
int kcfg_setting_show(undefined8 *param_1)

{
  char *__format;
  undefined8 uVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  ulong uVar5;
  
  iVar2 = get_modem_is_enabled();
  uVar1 = CONCAT71(s__modem_num__1_00110abf._8_7_,s__modem_num__1_00110abf[7]);
  uVar3 = 0x30;
  if (iVar2 != 0) {
    uVar3 = 0x31;
  }
  *param_1 = CONCAT17(s__modem_num__1_00110abf[7],s__modem_num__1_00110abf._0_7_);
  *(undefined8 *)((long)param_1 + 7) = uVar1;
  uVar3 = snprintf((char *)((long)param_1 + 0xe),0xfe2,"[modem en]:%c\n",(ulong)uVar3);
  if ((int)uVar3 < 0) {
    ccci_dump_write(0,1,"[0/util]%s-%d:snprintf fail,actual_write=%d\n","kcfg_setting_show",0x10a,
                    uVar3);
    _printk(&DAT_0010f255,"kcfg_setting_show",0x10a,uVar3);
    uVar3 = 0;
  }
  else if (0xfe0 < uVar3) {
    uVar3 = 0xfe1;
  }
  iVar2 = check_rat_at_md_img(&DAT_0011152f);
  uVar4 = 0x30;
  if (iVar2 != 0) {
    uVar4 = 0x31;
  }
  uVar4 = snprintf((char *)((long)param_1 + (ulong)(uVar3 + 0xe)),(ulong)(0xff2 - uVar3),
                   "[MTK_ECCCI_C2K]:%c\n",(ulong)uVar4);
  if ((int)uVar4 < 0) {
    ccci_dump_write(0,1,"[0/util]%s-%d:snprintf fail,actual_write=%d\n","kcfg_setting_show",0x11a,
                    uVar4);
    _printk(&DAT_0010f255,"kcfg_setting_show",0x11a,uVar4);
    uVar4 = 0;
  }
  else if (0xff2 - uVar3 <= uVar4) {
    uVar4 = 0xff1 - uVar3;
  }
  uVar4 = uVar4 + uVar3 + 0xe;
  __format = "[ccci_drv_ver]:V2\n";
  if (ccci_port_ver != 6) {
    __format = "[ccci_drv_ver]:V1\n";
  }
  uVar3 = snprintf((char *)((long)param_1 + (ulong)uVar4),(ulong)(0x1000 - uVar4),__format);
  if ((int)uVar3 < 0) {
    ccci_dump_write(0,1,"[0/util]%s-%d:snprintf fail,actual_write=%d\n","kcfg_setting_show",299,
                    uVar3);
    _printk(&DAT_0010f255,"kcfg_setting_show",299,uVar3);
    uVar3 = 0;
  }
  else if (0x1000 - uVar4 <= uVar3) {
    uVar3 = 0xfff - uVar4;
  }
  uVar3 = uVar3 + uVar4;
  uVar5 = get_md_img_type();
  uVar4 = snprintf((char *)((long)param_1 + (ulong)uVar3),(ulong)(0x1000 - uVar3),
                   "[MTK_MD_CAP]:%d\n",uVar5 & 0xffffffff);
  if (0x1000 - uVar3 <= uVar4 || (int)uVar4 < 1) {
    uVar4 = 0;
  }
  uVar4 = uVar4 + uVar3;
  uVar3 = snprintf((char *)((long)param_1 + (ulong)uVar4),(ulong)(0x1000 - uVar4),"total:%d\n",
                   (ulong)uVar4);
  if ((int)uVar3 < 0) {
    ccci_dump_write(0,1,"[0/util]%s-%d:snprintf fail,actual_write=%d\n","kcfg_setting_show",0x13c,
                    uVar3);
    _printk(&DAT_0010f255,"kcfg_setting_show",0x13c,uVar3);
    uVar3 = 0;
  }
  else if (0x1000 - uVar4 <= uVar3) {
    uVar3 = 0xfff - uVar4;
  }
  iVar2 = uVar3 + uVar4;
  ccci_dump_write(0,1,"[0/util]cfg_info_buffer size:%d\n",iVar2);
  ccci_dump_write(2,1,"[0/util]cfg_info_buffer size:%d\n",iVar2);
  return iVar2;
}

