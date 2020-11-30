package com.qinglianyun.testapplication.bean;

import java.util.List;

/**
 * Created by tang_xqing on 2020/6/18.
 */
public class DeviceBean {

    /**
     * device_id : 222
     * project_name : 测试522
     * device_name : 自定义测试DTU
     * model_name : 自定义ceshi
     * project_id : 40
     * device_sn : zdytest
     * device_model_id : 123
     * desc :
     * create_time : 2020-06-09 16:45:25
     * online : yes
     * subdevs : [{"sub_device_name":"123","device_sub_id":"1","sub_type":"12","sub_ip":"","sub_port":"","sub_addr":"12312","protocol_type":null,"c_protocol":"1","datapoints":[{"name":"d","dp_id":"4"},{"name":"a","dp_id":"5"},{"name":"b","dp_id":"6"},{"name":"c","dp_id":"7"}],"info":{"subid":1,"version":"","active_time":1591700808,"online":"yes"}}]
     * info : {"online":"yes","subdevs":{"1":{"subid":1,"version":"","active_time":1591700808,"online":"yes"}},"subdevs_list":[{"subid":1,"version":"","active_time":1591700808,"online":"yes"}],"version":"00.01","city":"北京市"}
     * link : true
     * alarm : true
     */

    private String device_id;
    private String project_name;
    private String device_name;
    private String model_name;
    private String project_id;
    private String device_sn;
    private String device_model_id;
    private String desc;
    private String create_time;
    private String online;
    private InfoBean info;
    private boolean link;
    private boolean alarm;
    private List<SubdevsBeanX> subdevs;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getDevice_sn() {
        return device_sn;
    }

    public void setDevice_sn(String device_sn) {
        this.device_sn = device_sn;
    }

    public String getDevice_model_id() {
        return device_model_id;
    }

    public void setDevice_model_id(String device_model_id) {
        this.device_model_id = device_model_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public List<SubdevsBeanX> getSubdevs() {
        return subdevs;
    }

    public void setSubdevs(List<SubdevsBeanX> subdevs) {
        this.subdevs = subdevs;
    }

    public static class InfoBean {
        /**
         * online : yes
         * subdevs : {"1":{"subid":1,"version":"","active_time":1591700808,"online":"yes"}}
         * subdevs_list : [{"subid":1,"version":"","active_time":1591700808,"online":"yes"}]
         * version : 00.01
         * city : 北京市
         */

        private String online;
        private SubdevsBean subdevs;
        private String version;
        private String city;
        private List<SubdevsListBean> subdevs_list;

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public SubdevsBean getSubdevs() {
            return subdevs;
        }

        public void setSubdevs(SubdevsBean subdevs) {
            this.subdevs = subdevs;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public List<SubdevsListBean> getSubdevs_list() {
            return subdevs_list;
        }

        public void setSubdevs_list(List<SubdevsListBean> subdevs_list) {
            this.subdevs_list = subdevs_list;
        }

        public static class SubdevsBean {
            /**
             * 1 : {"subid":1,"version":"","active_time":1591700808,"online":"yes"}
             */

            @com.google.gson.annotations.SerializedName("1")
            private _$1Bean _$1;

            public _$1Bean get_$1() {
                return _$1;
            }

            public void set_$1(_$1Bean _$1) {
                this._$1 = _$1;
            }

            public static class _$1Bean {
                /**
                 * subid : 1
                 * version :
                 * active_time : 1591700808
                 * online : yes
                 */

                private int subid;
                private String version;
                private int active_time;
                private String online;

                public int getSubid() {
                    return subid;
                }

                public void setSubid(int subid) {
                    this.subid = subid;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }

                public int getActive_time() {
                    return active_time;
                }

                public void setActive_time(int active_time) {
                    this.active_time = active_time;
                }

                public String getOnline() {
                    return online;
                }

                public void setOnline(String online) {
                    this.online = online;
                }
            }
        }

        public static class SubdevsListBean {
            /**
             * subid : 1
             * version :
             * active_time : 1591700808
             * online : yes
             */

            private int subid;
            private String version;
            private int active_time;
            private String online;

            public int getSubid() {
                return subid;
            }

            public void setSubid(int subid) {
                this.subid = subid;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public int getActive_time() {
                return active_time;
            }

            public void setActive_time(int active_time) {
                this.active_time = active_time;
            }

            public String getOnline() {
                return online;
            }

            public void setOnline(String online) {
                this.online = online;
            }
        }
    }

    public static class SubdevsBeanX {
        /**
         * sub_device_name : 123
         * device_sub_id : 1
         * sub_type : 12
         * sub_ip :
         * sub_port :
         * sub_addr : 12312
         * protocol_type : null
         * c_protocol : 1
         * datapoints : [{"name":"d","dp_id":"4"},{"name":"a","dp_id":"5"},{"name":"b","dp_id":"6"},{"name":"c","dp_id":"7"}]
         * info : {"subid":1,"version":"","active_time":1591700808,"online":"yes"}
         */

        private String sub_device_name;
        private String device_sub_id;
        private String sub_type;
        private String sub_ip;
        private String sub_port;
        private String sub_addr;
        private Object protocol_type;
        private String c_protocol;
        private InfoBeanX info;
        private List<DatapointsBean> datapoints;

        public String getSub_device_name() {
            return sub_device_name;
        }

        public void setSub_device_name(String sub_device_name) {
            this.sub_device_name = sub_device_name;
        }

        public String getDevice_sub_id() {
            return device_sub_id;
        }

        public void setDevice_sub_id(String device_sub_id) {
            this.device_sub_id = device_sub_id;
        }

        public String getSub_type() {
            return sub_type;
        }

        public void setSub_type(String sub_type) {
            this.sub_type = sub_type;
        }

        public String getSub_ip() {
            return sub_ip;
        }

        public void setSub_ip(String sub_ip) {
            this.sub_ip = sub_ip;
        }

        public String getSub_port() {
            return sub_port;
        }

        public void setSub_port(String sub_port) {
            this.sub_port = sub_port;
        }

        public String getSub_addr() {
            return sub_addr;
        }

        public void setSub_addr(String sub_addr) {
            this.sub_addr = sub_addr;
        }

        public Object getProtocol_type() {
            return protocol_type;
        }

        public void setProtocol_type(Object protocol_type) {
            this.protocol_type = protocol_type;
        }

        public String getC_protocol() {
            return c_protocol;
        }

        public void setC_protocol(String c_protocol) {
            this.c_protocol = c_protocol;
        }

        public InfoBeanX getInfo() {
            return info;
        }

        public void setInfo(InfoBeanX info) {
            this.info = info;
        }

        public List<DatapointsBean> getDatapoints() {
            return datapoints;
        }

        public void setDatapoints(List<DatapointsBean> datapoints) {
            this.datapoints = datapoints;
        }

        public static class InfoBeanX {
            /**
             * subid : 1
             * version :
             * active_time : 1591700808
             * online : yes
             */

            private int subid;
            private String version;
            private int active_time;
            private String online;

            public int getSubid() {
                return subid;
            }

            public void setSubid(int subid) {
                this.subid = subid;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public int getActive_time() {
                return active_time;
            }

            public void setActive_time(int active_time) {
                this.active_time = active_time;
            }

            public String getOnline() {
                return online;
            }

            public void setOnline(String online) {
                this.online = online;
            }
        }

        public static class DatapointsBean {
            /**
             * name : d
             * dp_id : 4
             */

            private String name;
            private String dp_id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDp_id() {
                return dp_id;
            }

            public void setDp_id(String dp_id) {
                this.dp_id = dp_id;
            }
        }
    }
}
