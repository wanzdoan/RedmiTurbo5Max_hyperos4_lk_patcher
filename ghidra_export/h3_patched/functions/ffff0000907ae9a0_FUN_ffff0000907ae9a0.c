// ffff0000907ae9a0  size=360  FUN_ffff0000907ae9a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907ae9a0(long param_1,ulong param_2)

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
  
  uVar1 = DAT_ffff000090837ea4;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  iVar2 = FUN_ffff0000907add00
                    (param_1,param_2,_DAT_ffff00009090c55c - (int)param_2,&local_c0,
                     DAT_ffff000090837ea4);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff0000907acfd8(0xffff00009090ca30,&local_c0,uVar1);
    uVar1 = DAT_ffff000090837ea4;
    if (iVar2 != 0) {
      local_50 = 0;
      uStack_48 = 0;
      local_60 = 0;
      uStack_58 = 0;
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      FUN_ffff00009077de54(s___s__SW__Image_auth__ffff0000907dbbbf,&DAT_ffff0000907f20e7);
      if (((param_1 == 0) ||
          (iVar2 = FUN_ffff0000907ade60
                             (param_1,param_2 & 0xffffffff,_DAT_ffff00009090c55c - (int)param_2,
                              &local_80,uVar1), iVar2 != 0)) ||
         (iVar2 = FUN_ffff0000907acfd8(0xffff00009090ca30,&local_80,uVar1), iVar2 != 0)) {
        iVar2 = 0x101011;
        goto LAB_ffff0000907aea80;
      }
    }
    FUN_ffff00009077de54
              (s___s__image__s_auth_pass_ffff0000907d068c,&DAT_ffff0000907f20e7,0xffff00009090c564);
    iVar2 = FUN_ffff0000907ad9d0(param_1,param_2 & 0xffffffff);
    if (_DAT_ffff00009090c50c != 0) {
      FUN_ffff000090745ebc();
    }
  }
  else {
LAB_ffff0000907aea80:
    FUN_ffff00009077de54
              (s___s__image__s_auth_fail__0x_x__ffff0000907f39f5,&DAT_ffff0000907f20e7,
               0xffff00009090c564,iVar2);
    FUN_ffff0000907aeff0();
  }
  FUN_ffff0000907b791c();
  return iVar2;
}


