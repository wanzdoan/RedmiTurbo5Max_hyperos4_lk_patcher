// ffff000090734590  size=420  FUN_ffff000090734590

int FUN_ffff000090734590(long param_1,int *param_2,undefined8 param_3)

{
  int iVar1;
  undefined4 uVar2;
  int iVar3;
  undefined8 uVar4;
  
  iVar1 = FUN_ffff00009077bdf4();
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__s__d___PART_UTILS__Could_not_re_ffff0000907e2c77,s_get_gpt_header_ffff0000907d2c35
               ,0x27);
    return iVar1;
  }
  if ((*param_2 == 0x20494645) && (param_2[1] == 0x54524150)) {
    iVar1 = param_2[4];
    param_2[4] = 0;
    uVar2 = FUN_ffff00009076c7c4(0,0,0);
    iVar3 = FUN_ffff00009076c7c4(uVar2,param_2,param_2[3]);
    if (iVar1 == iVar3) {
      param_2[4] = iVar1;
      iVar1 = param_2[0x15] * param_2[0x14];
      uVar4 = FUN_ffff00009077d294(0x40,iVar1);
      iVar3 = FUN_ffff00009077bdf4
                        (param_1,uVar4,*(long *)(param_1 + 0x28) * *(long *)(param_2 + 0x12),iVar1);
      if (iVar3 < 0) {
        FUN_ffff00009077de54(s__PART_UTILS__read_entry_failed_e_ffff0000907d61b1,iVar3);
        thunk_FUN_ffff0000907c2ad8(uVar4);
        return iVar3;
      }
      uVar2 = FUN_ffff00009076c7c4(0,0,0);
      iVar1 = FUN_ffff00009076c7c4(uVar2,uVar4,iVar1);
      if (param_2[0x16] == iVar1) {
        thunk_FUN_ffff0000907c2ad8(uVar4);
        return 0;
      }
      FUN_ffff00009077de54
                (s__s_check_header__err_entries_crc_ffff0000907ee0b6,
                 s_get_gpt_header_ffff0000907d2c35);
      thunk_FUN_ffff0000907c2ad8(uVar4);
    }
    else {
      FUN_ffff00009077de54
                (s__s_check_header_in__llu__err_crc_ffff0000907d617d,
                 s_get_gpt_header_ffff0000907d2c35,param_3,iVar1);
    }
  }
  return 1;
}


