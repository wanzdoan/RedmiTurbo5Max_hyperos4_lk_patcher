// ffff0000907ac154  size=600  FUN_ffff0000907ac154

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907ac154(uint param_1)

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
  if (_DAT_ffff00009090af2c == 0) {
    iVar4 = FUN_ffff0000907ab798();
    if (iVar4 == 0) {
      iVar4 = 0x104009;
      if (DAT_ffff00009090af30 == '\x01') {
        uVar6 = (ulong)_DAT_ffff00009090af34;
        if (_DAT_ffff00009090af34 != 0) {
          piVar7 = &DAT_ffff000090837548;
          do {
            if (piVar7[-2] == 4) {
              _DAT_ffff00009090af2c = *piVar7;
              goto LAB_ffff0000907ac184;
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
    iVar3 = FUN_ffff0000907ad53c();
    if (iVar3 != 0) {
      FUN_ffff00009077de54
                (s___s__Get_subgroup_index_failed__r_ffff0000907f398d,&DAT_ffff0000907f20fe,iVar4);
      return;
    }
  }
  else {
LAB_ffff0000907ac184:
    iVar4 = _DAT_ffff00009090af2c;
    iVar3 = FUN_ffff0000907acc50(_DAT_ffff00009090af2c,&local_34);
    if (iVar3 == 0) {
      uVar2 = (local_34 & 0xff) - (local_34 >> 1 & 0x55);
      uVar2 = (uVar2 & 0x33333333) + (uVar2 >> 2 & 0x33333333);
      uVar2 = (uVar2 + (uVar2 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18;
      if (uVar2 < param_1) {
        if (7 < param_1) {
          iVar3 = FUN_ffff0000907ad53c();
          if (iVar3 != 0) {
            FUN_ffff00009077de54
                      (s___s__da_ver___d__is_overflow_set_ffff0000907dd57f,&DAT_ffff0000907f20fe,
                       param_1);
          }
          param_1 = 7;
        }
        uVar2 = -1 << (ulong)(param_1 & 0x1f);
        uVar1 = uVar2 ^ local_34 & 0xff ^ 0xffffffff;
        if (uVar1 == 0) {
          return;
        }
        iVar3 = FUN_ffff0000907acc24(iVar4,uVar1);
        if (iVar3 != 0) {
          iVar4 = FUN_ffff0000907ad53c();
          if (iVar4 == 0) {
            return;
          }
          FUN_ffff00009077de54
                    (s___s__Write_arb_otp_ver_failed__r_ffff0000907d6fd3,&DAT_ffff0000907f20fe,iVar3
                    );
          return;
        }
        iVar4 = FUN_ffff0000907acc50(iVar4,&local_38);
        if (iVar4 != 0) {
          iVar3 = FUN_ffff0000907ad53c();
          if (iVar3 == 0) {
            return;
          }
          FUN_ffff00009077de54
                    (s___s__Read_arb_otp_ver_failed__re_ffff00009080193f,&DAT_ffff0000907f20fe,iVar4
                    );
          return;
        }
        if ((uVar1 & (local_38 ^ 0xffffffff)) == 0) {
          return;
        }
        iVar4 = FUN_ffff0000907ad53c();
        if (iVar4 == 0) {
          return;
        }
        uVar2 = ~uVar2;
        pcVar5 = s___s__write_0x_x__read_0x_x_ffff0000907d063f;
        param_1 = local_38;
      }
      else {
        iVar4 = FUN_ffff0000907ad53c();
        if (iVar4 == 0) {
          return;
        }
        pcVar5 = s___s__otp_ver__d__>__da_ver__d___d_ffff0000907ed610;
      }
      FUN_ffff00009077de54(pcVar5,&DAT_ffff0000907f20fe,uVar2,param_1);
    }
    else {
      iVar4 = FUN_ffff0000907ad53c();
      if (iVar4 != 0) {
        FUN_ffff00009077de54(s___s__Read_arb_otp_ver_failed__ffff0000907fab2b,&DAT_ffff0000907f20fe)
        ;
      }
    }
  }
  return;
}


