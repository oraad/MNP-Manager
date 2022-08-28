import classNames from 'classnames'
import { FC } from 'react'
import './loadingbar.css'

interface LoadingBarProps {

}

export const LoadingBar: FC<LoadingBarProps> = () => {
    return (
        <div className={classNames('progress-line',
        'w-full h-1 m-0 flex bg-blue-200',
        'before:w-full before:h-1 before:m-0',
        'before:bg-blue-600 before:content-[\'\']')} />
    )
}
