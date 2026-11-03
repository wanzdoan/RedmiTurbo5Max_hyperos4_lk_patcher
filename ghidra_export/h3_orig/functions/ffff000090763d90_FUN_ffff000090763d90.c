// ffff000090763d90  size=392  FUN_ffff000090763d90

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090763d90(uint param_1)

{
  uint uVar1;
  ulong uVar2;
  undefined8 uVar3;
  
  uVar2 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c1b,&DAT_ffff00009087cb50,0x400,0x3c);
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa517,
               s_CheckBlackboxLogTableHeader_ffff0000907e518b,0x4a5,s_BLACKBOX_ffff0000907d9988);
    uVar3 = 0x6d3;
    param_1 = 0;
    goto LAB_ffff000090763ec4;
  }
  if (_DAT_ffff00009087cb50 != 0x56775af41bcde0f3) {
    FUN_ffff00009077de54
              (s__s__d____s___init_log_table_head_ffff0000907e9c2a,
               s_CheckBlackboxLogTableHeader_ffff0000907e518b,0x4ab,s_BLACKBOX_ffff0000907d9988);
    FUN_ffff0000907609d4(&DAT_ffff00009087cb50);
  }
  FUN_ffff0000907612e8(&DAT_ffff00009087cb50);
  if (param_1 == 1) {
    uVar2 = FUN_ffff000090762f0c();
    if ((uVar2 & 1) == 0) {
LAB_ffff000090763ea4:
      uVar3 = 0x6e5;
LAB_ffff000090763ec4:
      FUN_ffff00009077de54
                (s__s__d____s___check_blackbox_head_ffff0000907fddf6,
                 s_save_logs_to_blackbox_ffff0000907ecdf1,uVar3,s_BLACKBOX_ffff0000907d9988,param_1)
      ;
      return;
    }
    uVar1 = 1;
    uVar3 = 2;
  }
  else {
    if (param_1 == 2) {
      uVar2 = FUN_ffff000090762ff8();
joined_r0xffff000090763e88:
      if ((uVar2 & 1) == 0) goto LAB_ffff000090763ea4;
    }
    else {
      if (param_1 == 3) {
        uVar2 = FUN_ffff0000907630e4();
        goto joined_r0xffff000090763e88;
      }
      if ((param_1 & 0xfffffffe) != 2) goto LAB_ffff000090763f04;
    }
    FUN_ffff0000907632e8(param_1,0);
    FUN_ffff0000907632e8(param_1,2);
    uVar3 = 1;
    uVar1 = param_1;
  }
  FUN_ffff0000907632e8(uVar1,uVar3);
LAB_ffff000090763f04:
  FUN_ffff0000907639c8(param_1);
  return;
}


