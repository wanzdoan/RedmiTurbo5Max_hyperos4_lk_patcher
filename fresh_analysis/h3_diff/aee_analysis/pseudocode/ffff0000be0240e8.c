
undefined8 FUN_ffff0000be0240e8(undefined8 param_1,undefined4 param_2,undefined8 *param_3)

{
  undefined4 uVar1;
  undefined8 uVar2;
  long lVar3;
  uint local_38;
  char local_34;
  
  local_34 = '\0';
  local_38 = 0;
  DAT_ffff0000be117ba0 =
       FUN_ffff0000be05f2c0(DAT_ffff0000be117ba0,(long)(int)DAT_ffff0000be117b98 * 0x20 + 0x20);
  if (DAT_ffff0000be117ba0 == 0) {
    uVar2 = 0xffffffff;
  }
  else {
    DAT_ffff0000be117ba8 = param_1;
    *(undefined8 *)(DAT_ffff0000be117ba0 + (long)(int)DAT_ffff0000be117b98 * 0x20) = *param_3;
    FUN_ffff0000be0237e0(param_1,param_2,s_pwm_config_ffff0000be0a33e7,&local_38);
    lVar3 = DAT_ffff0000be117ba0 + (long)(int)DAT_ffff0000be117b98 * 0x20;
    *(uint *)(lVar3 + 8) = local_38 & 0xff;
    *(uint *)(lVar3 + 0xc) = local_38 >> 8 & 0xff;
    *(uint *)(lVar3 + 0x10) = local_38 >> 0x10 & 0xff;
    *(uint *)(lVar3 + 0x14) = local_38 >> 0x18;
    *(bool *)(lVar3 + 0x18) = local_34 != '\0';
    uVar1 = FUN_ffff0000be023794(param_1,param_2,s_gate_enable_ffff0000be0c085b,0);
    lVar3 = (long)(int)DAT_ffff0000be117b98;
    uVar2 = 0;
    DAT_ffff0000be117b98._0_4_ = (int)DAT_ffff0000be117b98 + 1;
    *(undefined4 *)(DAT_ffff0000be117ba0 + lVar3 * 0x20 + 0x1c) = uVar1;
  }
  return uVar2;
}


