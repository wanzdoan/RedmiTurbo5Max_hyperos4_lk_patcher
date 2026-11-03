// ffff0000907acda8  size=460  FUN_ffff0000907acda8

int FUN_ffff0000907acda8(ulong param_1,uint param_2)

{
  int iVar1;
  int iVar2;
  int *piVar3;
  ulong uVar4;
  uint local_24;
  
  uVar4 = param_1 & 0xffffffff;
  local_24 = 0;
  iVar1 = FUN_ffff0000907acc70(param_1,&local_24);
  if (iVar1 == 0) {
    FUN_ffff0000907b7930();
    FUN_ffff0000907b79a4();
    piVar3 = (int *)FUN_ffff0000907b7a18(0x4000);
    if (piVar3 == (int *)0x0) {
      iVar1 = 0x100007;
    }
    else {
      FUN_ffff000090788808(s_seccfg_ffff0000907d1c33,0x1000,piVar3,0x84);
      if (*piVar3 == 0x800208) {
        iVar1 = 0x104004;
        piVar3[uVar4 + 1] = local_24 | param_2;
        iVar2 = FUN_ffff0000907888a0(s_seccfg_ffff0000907d1c33,0x1000,piVar3,0x84);
        if (iVar2 == 0) {
          iVar2 = FUN_ffff000090788808(s_seccfg_ffff0000907d1c33,0x1000,piVar3 + 0x21,0x84);
          if (iVar2 == 0) {
            iVar1 = FUN_ffff00009077e820(piVar3,piVar3 + 0x21,0x84);
            if (iVar1 == 0) {
              iVar1 = FUN_ffff0000907ad53c();
              if (iVar1 != 0) {
                FUN_ffff00009077de54
                          (s___s__write_idx__d_pass__otp___0x_ffff0000907e21e5,&DAT_ffff0000907f20fe
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
        iVar1 = FUN_ffff0000907ad53c();
        if (iVar1 != 0) {
          FUN_ffff00009077de54(s_magic_num_is_not_match_ffff0000907d6ffd);
        }
        iVar1 = FUN_ffff0000907ad53c();
        if (iVar1 != 0) {
          FUN_ffff00009077de54
                    (s___s__read_magic_num___0x_x_ffff0000907e570f,&DAT_ffff0000907f20fe,*piVar3);
        }
        iVar1 = 0x104004;
        iVar2 = FUN_ffff0000907ad53c();
        if (iVar2 != 0) {
          FUN_ffff00009077de54
                    (s___s__correct___0x_x_ffff0000907ea27c,&DAT_ffff0000907f20fe,0x800208);
        }
      }
    }
  }
  iVar2 = FUN_ffff0000907ad53c();
  if (iVar2 != 0) {
    FUN_ffff00009077de54
              (s___s__write_idx__d_fail__0x_x__ffff0000907fc74f,&DAT_ffff0000907f20fe,uVar4,iVar1);
  }
  return iVar1;
}


