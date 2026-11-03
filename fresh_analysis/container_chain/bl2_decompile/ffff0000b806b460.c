
int FUN_ffff0000b806b460(long param_1,ulong param_2)

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
  
  uVar1 = DAT_ffff0000b81076ec;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  iVar2 = FUN_ffff0000b806a7c0
                    (param_1,param_2,DAT_ffff0000b81b8854 - (int)param_2,&local_c0,
                     DAT_ffff0000b81076ec);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff0000b8069a90(&DAT_ffff0000b81b8d28,&local_c0,uVar1);
    uVar1 = DAT_ffff0000b81076ec;
    if (iVar2 != 0) {
      local_50 = 0;
      uStack_48 = 0;
      local_60 = 0;
      uStack_58 = 0;
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      FUN_ffff0000b804c33c(s___s__SW__Image_auth__ffff0000b808c105,&DAT_ffff0000b80987e6);
      if (((param_1 == 0) ||
          (iVar2 = FUN_ffff0000b806a920
                             (param_1,param_2 & 0xffffffff,DAT_ffff0000b81b8854 - (int)param_2,
                              &local_80,uVar1), iVar2 != 0)) ||
         (iVar2 = FUN_ffff0000b8069a90(&DAT_ffff0000b81b8d28,&local_80,uVar1), iVar2 != 0)) {
        iVar2 = 0x101011;
        goto LAB_ffff0000b806b540;
      }
    }
    FUN_ffff0000b804c33c
              (s___s__image__s_auth_pass_ffff0000b8085d57,&DAT_ffff0000b80987e6,
               &DAT_ffff0000b81b885c);
    iVar2 = FUN_ffff0000b806a490(param_1,param_2 & 0xffffffff);
    if (DAT_ffff0000b81b8804 != 0) {
      FUN_ffff0000b802c208();
    }
  }
  else {
LAB_ffff0000b806b540:
    FUN_ffff0000b804c33c
              (s___s__image__s_auth_fail__0x_x__ffff0000b8099652,&DAT_ffff0000b80987e6,
               &DAT_ffff0000b81b885c,iVar2);
    FUN_ffff0000b806bab0();
  }
  FUN_ffff0000b8073158();
  return iVar2;
}


