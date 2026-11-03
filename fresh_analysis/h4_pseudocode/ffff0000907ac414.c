
void FUN_ffff0000907ac414(uint param_1)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  char *pcVar5;
  ulong uVar6;
  int *piVar7;
  uint local_38;
  uint local_34;
  
  local_34 = 0;
  local_38 = 0;
  if (DAT_ffff00009090afec == 0) {
    iVar4 = FUN_ffff0000907aba58();
    if (iVar4 == 0) {
      iVar4 = 0x104009;
      if (DAT_ffff00009090aff0 == '\x01') {
        uVar6 = (ulong)DAT_ffff00009090aff4;
        if (DAT_ffff00009090aff4 != 0) {
          piVar7 = &DAT_ffff000090837568;
          do {
            if (piVar7[-2] == 4) {
              DAT_ffff00009090afec = *piVar7;
              goto LAB_ffff0000907ac444;
            }
            piVar7 = piVar7 + 7;
            uVar6 = uVar6 - 1;
          } while (uVar6 != 0);
        }
      }
      else {
        iVar4 = 0x10400c;
      }
    }
    iVar3 = FUN_ffff0000907ad7fc();
    if (iVar3 != 0) {
      FUN_ffff00009077e11c
                (s___s__Get_subgroup_index_failed__r_ffff0000907f3b19,&DAT_ffff0000907f228a,iVar4);
      return;
    }
  }
  else {
LAB_ffff0000907ac444:
    iVar4 = DAT_ffff00009090afec;
    iVar3 = FUN_ffff0000907acf10(DAT_ffff00009090afec,&local_34);
    if (iVar3 == 0) {
      uVar2 = (local_34 & 0xff) - (local_34 >> 1 & 0x55);
      uVar2 = (uVar2 & 0x33333333) + (uVar2 >> 2 & 0x33333333);
      uVar2 = (uVar2 + (uVar2 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18;
      if (uVar2 < param_1) {
        if (7 < param_1) {
          iVar3 = FUN_ffff0000907ad7fc();
          if (iVar3 != 0) {
            FUN_ffff00009077e11c
                      (s___s__da_ver___d__is_overflow_set_ffff0000907dd622,&DAT_ffff0000907f228a,
                       param_1);
          }
          param_1 = 7;
        }
        uVar2 = -1 << (ulong)(param_1 & 0x1f);
        uVar1 = uVar2 ^ local_34 & 0xff ^ 0xffffffff;
        if (uVar1 == 0) {
          return;
        }
        iVar3 = FUN_ffff0000907acee4(iVar4,uVar1);
        if (iVar3 != 0) {
          iVar4 = FUN_ffff0000907ad7fc();
          if (iVar4 == 0) {
            return;
          }
          FUN_ffff00009077e11c
                    (s___s__Write_arb_otp_ver_failed__r_ffff0000907d7069,&DAT_ffff0000907f228a,iVar3
                    );
          return;
        }
        iVar4 = FUN_ffff0000907acf10(iVar4,&local_38);
        if (iVar4 != 0) {
          iVar3 = FUN_ffff0000907ad7fc();
          if (iVar3 == 0) {
            return;
          }
          FUN_ffff00009077e11c
                    (s___s__Read_arb_otp_ver_failed__re_ffff000090801b1b,&DAT_ffff0000907f228a,iVar4
                    );
          return;
        }
        if ((uVar1 & (local_38 ^ 0xffffffff)) == 0) {
          return;
        }
        iVar4 = FUN_ffff0000907ad7fc();
        if (iVar4 == 0) {
          return;
        }
        uVar2 = ~uVar2;
        pcVar5 = s___s__write_0x_x__read_0x_x_ffff0000907d065e;
        param_1 = local_38;
      }
      else {
        iVar4 = FUN_ffff0000907ad7fc();
        if (iVar4 == 0) {
          return;
        }
        pcVar5 = s___s__otp_ver__d__>__da_ver__d___d_ffff0000907ed793;
      }
      FUN_ffff00009077e11c(pcVar5,&DAT_ffff0000907f228a,uVar2,param_1);
    }
    else {
      iVar4 = FUN_ffff0000907ad7fc();
      if (iVar4 != 0) {
        FUN_ffff00009077e11c(s___s__Read_arb_otp_ver_failed__ffff0000907facc7,&DAT_ffff0000907f228a)
        ;
      }
    }
  }
  return;
}


