// ffff000090764028  size=392  FUN_ffff000090764028

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090764028(uint param_1)

{
  uint uVar1;
  ulong uVar2;
  undefined8 uVar3;
  
  uVar2 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&DAT_ffff00009087cbe8,0x400,0x3c);
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___GetPartitionInfo_fa_ffff0000907fa6b3,
               s_CheckBlackboxLogTableHeader_ffff0000907e528f,0x4a5,s_BLACKBOX_ffff0000907d9a1f);
    uVar3 = 0x6d3;
    param_1 = 0;
    goto LAB_ffff00009076415c;
  }
  if (_DAT_ffff00009087cbe8 != 0x56775af41bcde0f3) {
    FUN_ffff00009077e11c
              (s__s__d____s___init_log_table_head_ffff0000907e9d6e,
               s_CheckBlackboxLogTableHeader_ffff0000907e528f,0x4ab,s_BLACKBOX_ffff0000907d9a1f);
    FUN_ffff000090760c6c(&DAT_ffff00009087cbe8);
  }
  FUN_ffff000090761580(&DAT_ffff00009087cbe8);
  if (param_1 == 1) {
    uVar2 = FUN_ffff0000907631a4();
    if ((uVar2 & 1) == 0) {
LAB_ffff00009076413c:
      uVar3 = 0x6e5;
LAB_ffff00009076415c:
      FUN_ffff00009077e11c
                (s__s__d____s___check_blackbox_head_ffff0000907fdf9e,
                 s_save_logs_to_blackbox_ffff0000907ecf74,uVar3,s_BLACKBOX_ffff0000907d9a1f,param_1)
      ;
      return;
    }
    uVar1 = 1;
    uVar3 = 2;
  }
  else {
    if (param_1 == 2) {
      uVar2 = FUN_ffff000090763290();
joined_r0xffff000090764120:
      if ((uVar2 & 1) == 0) goto LAB_ffff00009076413c;
    }
    else {
      if (param_1 == 3) {
        uVar2 = FUN_ffff00009076337c();
        goto joined_r0xffff000090764120;
      }
      if ((param_1 & 0xfffffffe) != 2) goto LAB_ffff00009076419c;
    }
    FUN_ffff000090763580(param_1,0);
    FUN_ffff000090763580(param_1,2);
    uVar3 = 1;
    uVar1 = param_1;
  }
  FUN_ffff000090763580(uVar1,uVar3);
LAB_ffff00009076419c:
  FUN_ffff000090763c60(param_1);
  return;
}


