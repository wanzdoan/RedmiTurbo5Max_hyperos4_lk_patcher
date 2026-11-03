// ffff0000907ad068  size=460  FUN_ffff0000907ad068

int FUN_ffff0000907ad068(ulong param_1,uint param_2)

{
  int iVar1;
  int iVar2;
  int *piVar3;
  ulong uVar4;
  uint local_24;
  
  uVar4 = param_1 & 0xffffffff;
  local_24 = 0;
  iVar1 = FUN_ffff0000907acf30(param_1,&local_24);
  if (iVar1 == 0) {
    FUN_ffff0000907b7fa4();
    FUN_ffff0000907b8018();
    piVar3 = (int *)FUN_ffff0000907b808c(0x4000);
    if (piVar3 == (int *)0x0) {
      iVar1 = 0x100007;
    }
    else {
      FUN_ffff000090788ac8(s_seccfg_ffff0000907d1c70,0x1000,piVar3,0x84);
      if (*piVar3 == 0x800208) {
        iVar1 = 0x104004;
        piVar3[uVar4 + 1] = local_24 | param_2;
        iVar2 = FUN_ffff000090788b60(s_seccfg_ffff0000907d1c70,0x1000,piVar3,0x84);
        if (iVar2 == 0) {
          iVar2 = FUN_ffff000090788ac8(s_seccfg_ffff0000907d1c70,0x1000,piVar3 + 0x21,0x84);
          if (iVar2 == 0) {
            iVar1 = FUN_ffff00009077eae8(piVar3,piVar3 + 0x21,0x84);
            if (iVar1 == 0) {
              iVar1 = FUN_ffff0000907ad7fc();
              if (iVar1 != 0) {
                FUN_ffff00009077e11c
                          (s___s__write_idx__d_pass__otp___0x_ffff0000907e22b6,&DAT_ffff0000907f228a
                           ,uVar4,piVar3[uVar4 + 1]);
              }
              return 0;
            }
            iVar1 = 0x104005;
          }
        }
        else {
          iVar1 = 0x104005;
        }
      }
      else {
        iVar1 = FUN_ffff0000907ad7fc();
        if (iVar1 != 0) {
          FUN_ffff00009077e11c(s_magic_num_is_not_match_ffff0000907d7093);
        }
        iVar1 = FUN_ffff0000907ad7fc();
        if (iVar1 != 0) {
          FUN_ffff00009077e11c
                    (s___s__read_magic_num___0x_x_ffff0000907e5813,&DAT_ffff0000907f228a,*piVar3);
        }
        iVar1 = 0x104004;
        iVar2 = FUN_ffff0000907ad7fc();
        if (iVar2 != 0) {
          FUN_ffff00009077e11c
                    (s___s__correct___0x_x_ffff0000907ea3c0,&DAT_ffff0000907f228a,0x800208);
        }
      }
    }
  }
  iVar2 = FUN_ffff0000907ad7fc();
  if (iVar2 != 0) {
    FUN_ffff00009077e11c
              (s___s__write_idx__d_fail__0x_x__ffff0000907fc8eb,&DAT_ffff0000907f228a,uVar4,iVar1);
  }
  return iVar1;
}


