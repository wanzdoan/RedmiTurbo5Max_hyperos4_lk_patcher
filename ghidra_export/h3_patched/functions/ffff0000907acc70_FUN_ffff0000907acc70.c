// ffff0000907acc70  size=312  FUN_ffff0000907acc70

undefined4 FUN_ffff0000907acc70(ulong param_1,int *param_2)

{
  int iVar1;
  int *piVar2;
  undefined4 uVar3;
  
  uVar3 = 0x100006;
  if (param_2 != (int *)0x0) {
    *param_2 = -1;
    FUN_ffff0000907b7930();
    FUN_ffff0000907b79a4();
    piVar2 = (int *)FUN_ffff0000907b7a18(0x4000);
    if (piVar2 == (int *)0x0) {
      uVar3 = 0x100007;
    }
    else {
      uVar3 = 0x104004;
      iVar1 = FUN_ffff000090788808(s_seccfg_ffff0000907d1c33,0x1000,piVar2,0x84);
      if (iVar1 == 0) {
        if (*piVar2 == 0x800208) {
          *param_2 = piVar2[(param_1 & 0xffffffff) + 1];
          return 0;
        }
        iVar1 = FUN_ffff0000907ad53c();
        if (iVar1 != 0) {
          FUN_ffff00009077de54(s___s__magic_num_is_not_match_ffff0000907fe431,&DAT_ffff0000907f20e7)
          ;
        }
        iVar1 = FUN_ffff0000907ad53c();
        if (iVar1 != 0) {
          FUN_ffff00009077de54
                    (s___s__read_magic_num___0x_x_ffff0000907e56f8,&DAT_ffff0000907f20e7,*piVar2);
        }
        iVar1 = FUN_ffff0000907ad53c();
        if (iVar1 != 0) {
          FUN_ffff00009077de54
                    (s___s__correct___0x_x_ffff0000907ea265,&DAT_ffff0000907f20e7,0x800208);
        }
      }
    }
    iVar1 = FUN_ffff0000907ad53c();
    if (iVar1 != 0) {
      FUN_ffff00009077de54
                (s___s__read_idx__d_fail__0x_x__ffff0000907d2203,&DAT_ffff0000907f20e7,
                 param_1 & 0xffffffff,uVar3);
    }
  }
  return uVar3;
}


