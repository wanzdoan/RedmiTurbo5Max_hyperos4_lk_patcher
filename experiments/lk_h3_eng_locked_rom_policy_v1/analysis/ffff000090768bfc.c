
void FUN_ffff000090768bfc(undefined8 param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 uStack_130;
  undefined8 uStack_128;
  undefined8 uStack_120;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  undefined8 uStack_100;
  undefined8 uStack_f8;
  undefined8 uStack_f0;
  undefined8 uStack_e8;
  undefined8 uStack_e0;
  undefined8 uStack_d8;
  undefined8 uStack_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined8 uStack_48;
  undefined8 uStack_40;
  undefined8 uStack_38;
  int iStack_18;
  uint uStack_14;

  uStack_14 = 0;
  iVar1 = FUN_ffff00009077f990(param_1,&DAT_ffff0000907d788e);
  if (iVar1 == 0) goto LAB_ffff000090768e80;
  lVar3 = FUN_ffff000090721c80(s_bypass_check_imgkey_ffff0000907cbefe);
  iVar1 = FUN_ffff000090721db0();
  if ((((iVar1 != 0) && (lVar3 != 0)) &&
      (iVar1 = FUN_ffff00009077f990(lVar3,&DAT_ffff0000907d78b3), iVar1 == 0)) ||
     (iVar1 = FUN_ffff00009077fc2c(param_1,s_factory_ffff0000907d5669,8), iVar1 == 0))
  goto LAB_ffff000090768e80;
  iStack_18 = 0;
  uStack_40 = 0;
  uStack_38 = 0;
  uStack_50 = 0;
  uStack_48 = 0;
  uStack_60 = 0;
  uStack_58 = 0;
  uStack_70 = 0;
  uStack_68 = 0;
  uStack_80 = 0;
  uStack_78 = 0;
  uStack_90 = 0;
  uStack_88 = 0;
  uStack_a0 = 0;
  uStack_98 = 0;
  uStack_b0 = 0;
  uStack_a8 = 0;
  uStack_c0 = 0;
  uStack_b8 = 0;
  uStack_d0 = 0;
  uStack_c8 = 0;
  uStack_e0 = 0;
  uStack_d8 = 0;
  uStack_f0 = 0;
  uStack_e8 = 0;
  uStack_100 = 0;
  uStack_f8 = 0;
  uStack_110 = 0;
  uStack_108 = 0;
  uStack_120 = 0;
  uStack_118 = 0;
  uStack_130 = 0;
  uStack_128 = 0;
  iVar1 = FUN_ffff00009073585c();
  iVar2 = 0;
  if (iVar1 == 2) {
    iVar2 = FUN_ffff000090795cec(0,0,&uStack_130,0x100,&iStack_18);
    if (iVar2 == -6) {
      FUN_ffff00009077d808(100000);
      iVar1 = FUN_ffff00009073585c();
      if (iVar1 != 2) {
LAB_ffff000090768e14:
        iVar2 = -6;
        goto LAB_ffff000090768e18;
      }
      iVar2 = FUN_ffff000090795cec(0,0,&uStack_130,0x100,&iStack_18);
      if (iVar2 == -6) {
        FUN_ffff00009077d808(100000);
        iVar1 = FUN_ffff00009073585c();
        if (iVar1 != 2) goto LAB_ffff000090768e14;
        iVar2 = FUN_ffff000090795cec(0,0,&uStack_130,0x100,&iStack_18);
        if (iVar2 == -6) {
          FUN_ffff00009077d808(100000);
          iVar1 = FUN_ffff00009073585c();
          if (iVar1 != 2) goto LAB_ffff000090768e14;
          iVar2 = FUN_ffff000090795cec(0,0,&uStack_130,0x100,&iStack_18);
          if (iVar2 == -6) {
            FUN_ffff00009077d808(100000);
            iVar1 = FUN_ffff00009073585c();
            if (iVar1 != 2) goto LAB_ffff000090768e14;
            iVar2 = FUN_ffff000090795cec(0,0,&uStack_130,0x100,&iStack_18);
            if (iVar2 == -6) {
              FUN_ffff00009077d808(100000);
              iVar1 = FUN_ffff00009073585c();
              if (iVar1 != 2) goto LAB_ffff000090768e14;
              iVar2 = FUN_ffff000090795cec(0,0,&uStack_130,0x100,&iStack_18);
            }
          }
        }
      }
    }
  }
  else {
LAB_ffff000090768e18:
    FUN_ffff000090709cfc(s_Not_support_bootdev_type_ffff0000907d20eb);
  }
  if ((iVar2 != 0) && (iStack_18 == 7)) {
    uVar4 = FUN_ffff000090709bc8(s_unlocked_ffff0000907ee940);
    iVar1 = FUN_ffff00009077f990(uVar4,&DAT_ffff0000907e9853);
    if (iVar1 == 0) {
      FUN_ffff000090709dac(s_Not_allow_flashing_when_rpmb_no_p_ffff0000907d0773);
      return;
    }
  }
  uStack_14 = thunk_FUN_ffff00009074bfa8(0x1b);
  if (((((uStack_14 ^ 0xffffffff) & 0xe) == 0) &&
      (iVar1 = FUN_ffff00009074b510(0,&uStack_14), iVar1 == 0)) &&
     (((char)uStack_14 == '\0' &&
      ((iVar1 = FUN_ffff00009077fc2c(param_1,&DAT_ffff0000907d87f2,3), iVar1 != 0 &&
       (iVar1 = FUN_ffff00009077fc2c(param_1,&DAT_ffff0000907f34d3,3), iVar1 != 0)))))) {
    FUN_ffff000090709dac(s_CN_devices_do_not_allow_flashing_ffff0000907f89a9);
    return;
  }
LAB_ffff000090768e80:
  FUN_ffff000090709cfc(s_allowed_ffff0000907f51da);
  FUN_ffff000090709dec(&DAT_ffff0000907d788e);
  return;
}
