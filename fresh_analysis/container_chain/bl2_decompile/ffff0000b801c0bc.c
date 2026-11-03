
undefined8 FUN_ffff0000b801c0bc(undefined8 param_1,undefined4 param_2,undefined8 *param_3)

{
  undefined4 uVar1;
  undefined8 uVar2;
  long lVar3;
  uint local_38;
  char local_34;
  
  local_34 = '\0';
  local_38 = 0;
  DAT_ffff0000b811b388 =
       FUN_ffff0000b804bdd0(DAT_ffff0000b811b388,(long)DAT_ffff0000b811b380 * 0x20 + 0x20);
  if (DAT_ffff0000b811b388 == 0) {
    uVar2 = 0xffffffff;
  }
  else {
    DAT_ffff0000b811b390 = param_1;
    *(undefined8 *)(DAT_ffff0000b811b388 + (long)DAT_ffff0000b811b380 * 0x20) = *param_3;
    FUN_ffff0000b801b720(param_1,param_2,s_pwm_config_ffff0000b80852ea,&local_38);
    lVar3 = DAT_ffff0000b811b388 + (long)DAT_ffff0000b811b380 * 0x20;
    *(uint *)(lVar3 + 8) = local_38 & 0xff;
    *(uint *)(lVar3 + 0xc) = local_38 >> 8 & 0xff;
    *(uint *)(lVar3 + 0x10) = local_38 >> 0x10 & 0xff;
    *(uint *)(lVar3 + 0x14) = local_38 >> 0x18;
    *(bool *)(lVar3 + 0x18) = local_34 != '\0';
    uVar1 = FUN_ffff0000b801b6d4(param_1,param_2,s_gate_enable_ffff0000b809c627,0);
    lVar3 = (long)DAT_ffff0000b811b380;
    uVar2 = 0;
    DAT_ffff0000b811b380 = DAT_ffff0000b811b380 + 1;
    *(undefined4 *)(DAT_ffff0000b811b388 + lVar3 * 0x20 + 0x1c) = uVar1;
  }
  return uVar2;
}


