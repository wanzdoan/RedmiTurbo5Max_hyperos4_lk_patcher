
int FUN_ffff0000907aec68(long param_1,ulong param_2)

{
  undefined4 uVar1;
  int iVar2;
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
  
  uVar1 = DAT_ffff000090837ec4;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  iVar2 = FUN_ffff0000907adfc8
                    (param_1,param_2,DAT_ffff00009090c61c - (int)param_2,&local_c0,
                     DAT_ffff000090837ec4);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff0000907ad298(&DAT_ffff00009090caf0,&local_c0,uVar1);
    uVar1 = DAT_ffff000090837ec4;
    if (iVar2 != 0) {
      local_50 = 0;
      uStack_48 = 0;
      local_60 = 0;
      uStack_58 = 0;
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      FUN_ffff00009077e11c(s___s__SW__Image_auth__ffff0000907dbce1,&DAT_ffff0000907f228a);
      if (((param_1 == 0) ||
          (iVar2 = FUN_ffff0000907ae128
                             (param_1,param_2 & 0xffffffff,DAT_ffff00009090c61c - (int)param_2,
                              &local_80,uVar1), iVar2 != 0)) ||
         (iVar2 = FUN_ffff0000907ad298(&DAT_ffff00009090caf0,&local_80,uVar1), iVar2 != 0)) {
        iVar2 = 0x101011;
        goto LAB_ffff0000907aed48;
      }
    }
    FUN_ffff00009077e11c
              (s___s__image__s_auth_pass_ffff0000907d06ab,&DAT_ffff0000907f228a,
               &DAT_ffff00009090c624);
    iVar2 = FUN_ffff0000907adc98(param_1,param_2 & 0xffffffff);
    if (DAT_ffff00009090c5cc != 0) {
      FUN_ffff000090746154();
    }
  }
  else {
LAB_ffff0000907aed48:
    FUN_ffff00009077e11c
              (s___s__image__s_auth_fail__0x_x__ffff0000907f3b98,&DAT_ffff0000907f228a,
               &DAT_ffff00009090c624,iVar2);
    FUN_ffff0000907af2b8();
  }
  FUN_ffff0000907b7f90();
  return iVar2;
}


