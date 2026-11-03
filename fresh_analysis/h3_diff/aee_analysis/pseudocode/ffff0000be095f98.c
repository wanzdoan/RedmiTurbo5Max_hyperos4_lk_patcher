
undefined8 FUN_ffff0000be095f98(undefined8 param_1,uint *param_2,uint *param_3)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  char *pcVar4;
  int local_34;
  
  local_34 = 0;
  FUN_ffff0000be05fd98(&DAT_ffff0000be0b835e,s_util_get_i2c_lcd_bias_ffff0000be0c0283);
  iVar2 = FUN_ffff0000be056824(param_1,0xffffffff,s_mediatek_gate_ic_i2c_ffff0000be0b5ec2);
  if (iVar2 < 0) {
    pcVar4 = s_get_fdt_i2c_lcd_bias_node_fail_ffff0000be0b4d24;
  }
  else {
    puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,&DAT_ffff0000be0ae6fc,&local_34);
    if ((puVar3 == (uint *)0x0) || (local_34 == 0)) {
      pcVar4 = s_get_i2c_lcd_bias_reg_fail_ffff0000be0ac1d4;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      *param_3 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar3 = (uint *)FUN_ffff0000be056494(param_1,iVar2,&DAT_ffff0000be0a66c9,&local_34);
      if ((puVar3 != (uint *)0x0) && (local_34 != 0)) {
        uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        *param_2 = uVar1 >> 0x10 | uVar1 << 0x10;
        FUN_ffff0000be05fd98
                  (s__s___addr____d__id____d_ffff0000be0b0700,
                   s_util_get_i2c_lcd_bias_ffff0000be0c0283,*param_3);
        return 0;
      }
      pcVar4 = s_get_i2c_lcd_bias_id_fail_ffff0000be0ae1f1;
    }
  }
  FUN_ffff0000be05fd98(pcVar4);
  return 0xffffffff;
}


