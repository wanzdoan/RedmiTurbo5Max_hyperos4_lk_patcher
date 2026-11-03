// ffff0000907cd9cc  size=564  FUN_ffff0000907cd9cc

undefined8 FUN_ffff0000907cd9cc(undefined4 param_1,undefined8 param_2)

{
  int iVar1;
  undefined8 uVar2;
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
  int local_14;
  
  local_14 = 0;
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
  iVar1 = FUN_ffff00009073527c();
  if (iVar1 == 2) {
    iVar1 = FUN_ffff00009079606c(0,param_1,&local_130,0x100,&local_14);
    if (iVar1 == -6) {
      FUN_ffff00009077cea0(100000);
      iVar1 = FUN_ffff00009073527c();
      if (iVar1 != 2) goto LAB_ffff0000907cdba8;
      iVar1 = FUN_ffff00009079606c(0,param_1,&local_130,0x100,&local_14);
      if (iVar1 == -6) {
        FUN_ffff00009077cea0(100000);
        iVar1 = FUN_ffff00009073527c();
        if (iVar1 != 2) goto LAB_ffff0000907cdba8;
        iVar1 = FUN_ffff00009079606c(0,param_1,&local_130,0x100,&local_14);
        if (iVar1 == -6) {
          FUN_ffff00009077cea0(100000);
          iVar1 = FUN_ffff00009073527c();
          if (iVar1 != 2) goto LAB_ffff0000907cdba8;
          iVar1 = FUN_ffff00009079606c(0,param_1,&local_130,0x100,&local_14);
          if (iVar1 == -6) {
            FUN_ffff00009077cea0(100000);
            iVar1 = FUN_ffff00009073527c();
            if (iVar1 != 2) goto LAB_ffff0000907cdba8;
            iVar1 = FUN_ffff00009079606c(0,param_1,&local_130,0x100,&local_14);
            if (iVar1 == -6) {
              FUN_ffff00009077cea0(100000);
              iVar1 = FUN_ffff00009073527c();
              if (iVar1 != 2) goto LAB_ffff0000907cdba8;
              iVar1 = FUN_ffff00009079606c(0,param_1,&local_130,0x100,&local_14);
            }
          }
        }
      }
    }
    if ((iVar1 == 0) || (local_14 != 7)) {
      if (iVar1 == 0) {
        FUN_ffff00009077eb20(param_2,&local_130,0x100);
        uVar2 = 0;
      }
      else {
        FUN_ffff00009077e11c(s_Error___rpmb_read_return__d_ffff00009080025d);
        uVar2 = 4;
      }
    }
    else {
      FUN_ffff00009077e11c(s_Error___rpmb_hmac_key_hasn_t_bee_ffff0000907dbe2b);
      uVar2 = 6;
    }
  }
  else {
LAB_ffff0000907cdba8:
    FUN_ffff00009077e11c(s_Error___no_ufs_or_emmc_ffff000090800245);
    uVar2 = 5;
  }
  return uVar2;
}


