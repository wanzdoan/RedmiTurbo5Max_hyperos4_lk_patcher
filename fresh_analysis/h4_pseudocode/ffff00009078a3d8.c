
undefined8 FUN_ffff00009078a3d8(undefined4 param_1,undefined8 param_2,uint param_3)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  int local_34;
  
  local_34 = 0;
  local_48 = 0;
  uStack_40 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  uStack_e0 = 0;
  local_f8 = 0;
  uStack_f0 = 0;
  local_108 = 0;
  uStack_100 = 0;
  local_118 = 0;
  uStack_110 = 0;
  local_128 = 0;
  uStack_120 = 0;
  local_138 = 0;
  uStack_130 = 0;
  if ((param_3 < 0x101) || ((param_3 & 0xff) == 0)) {
    uVar2 = FUN_ffff00009078a1b8(param_1,&local_138,0x100);
    if ((int)uVar2 == 0) {
      FUN_ffff00009077eb20(&local_138,param_2,param_3);
      iVar1 = FUN_ffff00009073527c();
      if (iVar1 == 2) {
        iVar1 = FUN_ffff00009079652c(0,param_1,&local_138,0x100,&local_34);
        if (iVar1 == 7) {
          uVar2 = 6;
        }
        else if (iVar1 == 0 && local_34 == 0) {
          uVar2 = 0;
        }
        else {
          FUN_ffff00009077e11c(s_rpmb_write_return__d_ffff0000907ed3d1,iVar1);
          uVar2 = 4;
        }
      }
      else {
        uVar2 = 5;
      }
    }
  }
  else {
    FUN_ffff00009077e11c(s_wrong_buf_len____d_ffff0000907fa96e,param_3);
    uVar2 = 1;
  }
  return uVar2;
}


