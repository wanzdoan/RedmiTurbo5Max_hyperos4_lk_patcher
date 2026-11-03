
int FUN_ffff0000be027664(long param_1,int *param_2,undefined8 param_3)

{
  int iVar1;
  undefined4 uVar2;
  int iVar3;
  undefined8 uVar4;
  
  iVar1 = FUN_ffff0000be05e000();
  if (iVar1 < 0) {
    FUN_ffff0000be05fd98
              (s__s__d___PART_UTILS__Could_not_re_ffff0000be0b0bd4,s_get_gpt_header_ffff0000be0a5a9c
               ,0x27);
    return iVar1;
  }
  if ((*param_2 == 0x20494645) && (param_2[1] == 0x54524150)) {
    iVar1 = param_2[4];
    param_2[4] = 0;
    uVar2 = FUN_ffff0000be0553a4(0,0,0);
    iVar3 = FUN_ffff0000be0553a4(uVar2,param_2,param_2[3]);
    if (iVar1 == iVar3) {
      param_2[4] = iVar1;
      iVar1 = param_2[0x15] * param_2[0x14];
      uVar4 = FUN_ffff0000be05f204(0x40,iVar1);
      iVar3 = FUN_ffff0000be05e000
                        (param_1,uVar4,*(long *)(param_1 + 0x28) * *(long *)(param_2 + 0x12),iVar1);
      if (iVar3 < 0) {
        FUN_ffff0000be05fd98(s__PART_UTILS__read_entry_failed_e_ffff0000be0a7f33,iVar3);
        thunk_FUN_ffff0000be09937c(uVar4);
        return iVar3;
      }
      uVar2 = FUN_ffff0000be0553a4(0,0,0);
      iVar1 = FUN_ffff0000be0553a4(uVar2,uVar4,iVar1);
      if (param_2[0x16] == iVar1) {
        thunk_FUN_ffff0000be09937c(uVar4);
        return 0;
      }
      FUN_ffff0000be05fd98
                (s__s_check_header__err_entries_crc_ffff0000be0b89d8,
                 s_get_gpt_header_ffff0000be0a5a9c);
      thunk_FUN_ffff0000be09937c(uVar4);
    }
    else {
      FUN_ffff0000be05fd98
                (s__s_check_header_in__llu__err_crc_ffff0000be0a7eff,
                 s_get_gpt_header_ffff0000be0a5a9c,param_3,iVar1);
    }
  }
  return 1;
}


