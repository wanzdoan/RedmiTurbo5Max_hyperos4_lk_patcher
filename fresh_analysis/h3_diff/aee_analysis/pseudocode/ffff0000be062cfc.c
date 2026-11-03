
int FUN_ffff0000be062cfc(long param_1,int *param_2,undefined8 *param_3)

{
  undefined4 uVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  
  if ((*param_2 == 0x20494645) && (param_2[1] == 0x54524150)) {
    iVar3 = param_2[4];
    param_2[4] = 0;
    uVar1 = FUN_ffff0000be0553a4(0,0,0);
    iVar2 = FUN_ffff0000be0553a4(uVar1,param_2,param_2[3]);
    if (iVar3 == iVar2) {
      param_2[4] = iVar3;
      iVar3 = param_2[0x15] * param_2[0x14];
      uVar4 = FUN_ffff0000be05f204(0x40,iVar3);
      iVar2 = FUN_ffff0000be05e000
                        (param_1,uVar4,*(long *)(param_1 + 0x28) * *(long *)(param_2 + 0x12),iVar3);
      if (iVar2 < 0) {
        FUN_ffff0000be05fd98
                  (s___s__read_entry_failed_ffff0000be0ba44a,
                   s_partition_parse_gpt_header_ffff0000be0bb535);
        thunk_FUN_ffff0000be09937c(uVar4);
        return iVar2;
      }
      uVar1 = FUN_ffff0000be0553a4(0,0,0);
      iVar3 = FUN_ffff0000be0553a4(uVar1,uVar4,iVar3);
      if (param_2[0x16] == iVar3) {
        *(int *)((long)param_3 + 0xc) = param_2[3];
        *param_3 = *(undefined8 *)(param_2 + 10);
        *(int *)(param_3 + 2) = param_2[0x14];
        *(int *)(param_3 + 1) = param_2[0x15];
        thunk_FUN_ffff0000be09937c(uVar4);
        return 0;
      }
      FUN_ffff0000be05fd98
                (s__s_check_header__err_entries_crc_ffff0000be0b89d8,
                 s_partition_parse_gpt_header_ffff0000be0bb535);
      thunk_FUN_ffff0000be09937c(uVar4);
    }
    else {
      FUN_ffff0000be05fd98
                (s__s_check_header__err_crc_0x_x__0_ffff0000be0bd949,
                 s_partition_parse_gpt_header_ffff0000be0bb535,iVar3);
    }
  }
  return 1;
}


