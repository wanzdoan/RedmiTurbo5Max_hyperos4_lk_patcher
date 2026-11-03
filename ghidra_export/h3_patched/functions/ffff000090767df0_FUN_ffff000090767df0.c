// ffff000090767df0  size=772  FUN_ffff000090767df0

void FUN_ffff000090767df0(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  int local_18;
  uint local_14;
  
  local_14 = 0;
  iVar1 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907df27e);
  if (iVar1 == 0) goto LAB_ffff000090768074;
  lVar3 = FUN_ffff000090721af0(s_bypass_check_imgkey_ffff0000907d3de9);
  iVar1 = FUN_ffff000090721c20();
  if ((((iVar1 != 0) && (lVar3 != 0)) &&
      (iVar1 = FUN_ffff00009077ede4(lVar3,&DAT_ffff0000907df2a3), iVar1 == 0)) ||
     (iVar1 = FUN_ffff00009077f080(param_1,s_factory_ffff0000907dd17a,8), iVar1 == 0))
  goto LAB_ffff000090768074;
  local_18 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  iVar1 = FUN_ffff000090734fe4();
  iVar2 = 0;
  if (iVar1 == 2) {
    iVar2 = FUN_ffff000090795dac(0,0,&local_130,0x100,&local_18);
    if (iVar2 == -6) {
      FUN_ffff00009077cbd8(100000);
      iVar1 = FUN_ffff000090734fe4();
      if (iVar1 != 2) {
LAB_ffff000090768008:
        iVar2 = -6;
        goto LAB_ffff00009076800c;
      }
      iVar2 = FUN_ffff000090795dac(0,0,&local_130,0x100,&local_18);
      if (iVar2 == -6) {
        FUN_ffff00009077cbd8(100000);
        iVar1 = FUN_ffff000090734fe4();
        if (iVar1 != 2) goto LAB_ffff000090768008;
        iVar2 = FUN_ffff000090795dac(0,0,&local_130,0x100,&local_18);
        if (iVar2 == -6) {
          FUN_ffff00009077cbd8(100000);
          iVar1 = FUN_ffff000090734fe4();
          if (iVar1 != 2) goto LAB_ffff000090768008;
          iVar2 = FUN_ffff000090795dac(0,0,&local_130,0x100,&local_18);
          if (iVar2 == -6) {
            FUN_ffff00009077cbd8(100000);
            iVar1 = FUN_ffff000090734fe4();
            if (iVar1 != 2) goto LAB_ffff000090768008;
            iVar2 = FUN_ffff000090795dac(0,0,&local_130,0x100,&local_18);
            if (iVar2 == -6) {
              FUN_ffff00009077cbd8(100000);
              iVar1 = FUN_ffff000090734fe4();
              if (iVar1 != 2) goto LAB_ffff000090768008;
              iVar2 = FUN_ffff000090795dac(0,0,&local_130,0x100,&local_18);
            }
          }
        }
      }
    }
  }
  else {
LAB_ffff00009076800c:
    FUN_ffff000090709c60(s_Not_support_bootdev_type_ffff0000907d9c04);
  }
  if ((iVar2 != 0) && (local_18 == 7)) {
    uVar4 = FUN_ffff000090709b2c(s_unlocked_ffff0000907f5b24);
    iVar1 = FUN_ffff00009077ede4(uVar4,&DAT_ffff0000907f0b6f);
    if (iVar1 == 0) {
      FUN_ffff000090709d10(s_Not_allow_flashing_when_rpmb_no_p_ffff0000907d837d);
      return;
    }
  }
  local_14 = thunk_FUN_ffff00009074af94(0x1b);
  if (((((local_14 ^ 0xffffffff) & 0xe) == 0) &&
      (iVar1 = FUN_ffff00009074a4e8(0,&local_14), iVar1 == 0)) &&
     (((char)local_14 == '\0' &&
      ((iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907e0187,3), iVar1 != 0 &&
       (iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907fa556,3), iVar1 != 0)))))) {
    FUN_ffff000090709d10(s_CN_devices_do_not_allow_flashing_ffff0000907ff9c4);
    return;
  }
LAB_ffff000090768074:
  FUN_ffff000090709c60(s_allowed_ffff0000907fc1bc);
  FUN_ffff000090709d50(&DAT_ffff0000907df27e);
  return;
}


