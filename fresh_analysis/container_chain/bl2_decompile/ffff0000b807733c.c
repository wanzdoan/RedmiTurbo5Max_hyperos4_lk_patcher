
undefined8 FUN_ffff0000b807733c(undefined8 param_1,uint *param_2,uint *param_3)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  char *pcVar4;
  int local_34;
  
  local_34 = 0;
  FUN_ffff0000b804c33c(&DAT_ffff0000b8095dc6,s_util_get_i2c_lcd_bias_ffff0000b809c0c3);
  iVar2 = FUN_ffff0000b80483cc(param_1,0xffffffff,s_mediatek_gate_ic_i2c_ffff0000b8093f50);
  if (iVar2 < 0) {
    pcVar4 = s_get_fdt_i2c_lcd_bias_node_fail_ffff0000b8092fcb;
  }
  else {
    puVar3 = (uint *)FUN_ffff0000b804803c(param_1,iVar2,&DAT_ffff0000b808dfe3,&local_34);
    if ((puVar3 == (uint *)0x0) || (local_34 == 0)) {
      pcVar4 = s_get_i2c_lcd_bias_reg_fail_ffff0000b808c19d;
    }
    else {
      uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
      *param_3 = uVar1 >> 0x10 | uVar1 << 0x10;
      puVar3 = (uint *)FUN_ffff0000b804803c(param_1,iVar2,&DAT_ffff0000b8087c64,&local_34);
      if ((puVar3 != (uint *)0x0) && (local_34 != 0)) {
        uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
        *param_2 = uVar1 >> 0x10 | uVar1 << 0x10;
        FUN_ffff0000b804c33c
                  (s__s___addr____d__id____d_ffff0000b808f748,
                   s_util_get_i2c_lcd_bias_ffff0000b809c0c3,*param_3);
        return 0;
      }
      pcVar4 = s_get_i2c_lcd_bias_id_fail_ffff0000b808d9e5;
    }
  }
  FUN_ffff0000b804c33c(pcVar4);
  return 0xffffffff;
}


