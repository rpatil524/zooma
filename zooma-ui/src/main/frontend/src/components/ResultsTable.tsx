
import React, { Component, Fragment, ChangeEvent } from "react"
import { Row, Column } from "react-foundation"
import * as ZoomaApi from "../api/ZoomaApi"
import { ZoomaDatasources } from "../api/ZoomaDatasources"
import { sources } from '../data/sources.json'

interface Props {
    results:ZoomaApi.SearchResult[]
    datasources:ZoomaDatasources|undefined
}

interface State {
    hideUnmapped: boolean
}

export default class ResultsTable extends Component<Props, State> {

    constructor(props:any) {
        super(props)

        this.state = {
            hideUnmapped: false
        }
    }

    render() {

        let { results } = this.props

        return (
            <Fragment>
            <label>
                <input type="checkbox" checked={this.state.hideUnmapped} onClick={this.toggleHideUnmapped}>
                    Hide results that did not map
                </input>
            </label>
            <table>
                <thead>
                    <tr>
                        <th className="context-help">
                            <span className="context-help-label clickable" data-icon="?">Term Type </span>
                        </th>
                        <th className="context-help">
                            <span className="context-help-label clickable" data-icon="?">Term Value </span>
                        </th>
                        <th className="context-help">
                            <span className="context-help-label clickable" data-icon="?">Ontology Class Label </span>
                        </th>
                        <th className="context-help">
                            <span className="context-help-label clickable" data-icon="?">Mapping Confidence </span>
                        </th>
                        <th className="context-help">
                            <span className="context-help-label clickable" data-icon="?">Ontology Class ID </span>
                        </th>
                        <th className="context-help">
                            <span className="context-help-label clickable" data-icon="?">Source </span>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {
                    results
                    .filter(result => (!this.state.hideUnmapped) || result.mappingConfidence !== 'Did not map')
                    .map(result =>
                        <tr className={getResultClass(result)}>
                            <td>{result.propertyType}</td>
                            <td>{result.propertyValue}</td>
                            <td>{result.ontologyTermLabel}</td>
                            {/* <td>{result.ontologyTermSynonyms}</td> */}
                            <td>{result.mappingConfidence}</td>
                            <td>{result.ontologyTermID}</td>
                            <td><Datasource datasources={this.props.datasources} uri={result.datasource}/></td>
                            {/* <td>{result.ontologyIRI}</td> */}
                        </tr>
                    )}
                </tbody>
            </table>
            <p>
                <b>Stats:</b> {results.length} properties   
                {results.filter(r => r.mappingConfidence === 'High').length} high   
                {results.filter(r => r.mappingConfidence === 'Good').length} good   
                {results.filter(r => r.mappingConfidence === 'Medium').length} medium   
                {results.filter(r => r.mappingConfidence === 'Low').length} low 
                {results.filter(r => r.mappingConfidence === 'Did not map').length} unmapped
            </p>
            </Fragment>
        )
    }

    toggleHideUnmapped = () => {
        this.setState({ hideUnmapped: !this.state.hideUnmapped })
    }

}

function Datasource(props:any) {

    let { datasources, uri } = props

    if(datasources === undefined) {
        return <span>{uri}</span>
    }

    if(datasources.loadedOntologyURIs.indexOf(uri) !== -1) {

        let name = datasources.uriNameMap.get(uri)

        return (
            <a href={'//www.ebi.ac.uk/ols/ontologies/' + name} target="_blank">
                <img src="images/ols-logo.jpg" style={{height: '20px'}} alt={name} />
                &nbsp;
                { name }
            </a>
        )
    }

    let source = sources.filter(s => s.url === uri)[0]
    
    if(source !== undefined) {

        return (
            <a href={source.linkTo} target="_blank">
                <img src={source.logo} style={{height: '20px'}} alt={source.name} />
                &nbsp;
                { source.name }
            </a>
        )
    }

    return (
        <span>{uri}</span>
        // <a href={source.linkTo}>
        //     <img src={source.logo} />
        //     {source.name}
        // </a>
    )
}

function getResultClass(result) {

    return ({
        'High': 'automatic',
        'Good': 'curation',
        'Medium': 'curation',
        'Low': 'curation'
    })[result.mappingConfidence] || 'unmapped'

}



