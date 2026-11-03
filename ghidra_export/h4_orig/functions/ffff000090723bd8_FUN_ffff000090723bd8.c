// ffff000090723bd8  size=328  FUN_ffff000090723bd8

void FUN_ffff000090723bd8(code *param_1)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  lVar1 = FUN_ffff00009077d514(0x100000);
  if (lVar1 == 0) {
    FUN_ffff00009077e11c
              (s_dramc__malloc_failed_at__s_ffff0000907dc53b,s_save_fullk_data_ffff0000907ef67b);
    return;
  }
  lVar2 = FUN_ffff00009077c028(s_dram_para_ffff0000907ddc72);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s_dramc__Partition___s__is_not_exi_ffff0000907ddc7c,s_dram_para_ffff0000907ddc72);
  }
  else {
    uVar5 = 0xfffffffffff00000;
    do {
      FUN_ffff00009077ee50(lVar1,0,0x100000);
      uVar3 = FUN_ffff00009077c0bc(lVar2,lVar1,uVar5 + 0x200000,0x100000);
      if (uVar3 >> 0x14 == 0) {
        FUN_ffff000090781ae8(s_dramc__WARN___read_size_from_dra_ffff0000907cf433,uVar3);
        FUN_ffff00009077e11c(s_dramc__WARN___read_size_from_dra_ffff0000907cf433,uVar3);
        break;
      }
      uVar4 = FUN_ffff00009077b334(lVar1);
      uVar3 = (*param_1)(uVar4,uVar3);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077e11c
                  (s__s__failed_to_set_fullk_data__ffff0000907e7737,
                   s_save_fullk_data_ffff0000907ef67b);
      }
      uVar5 = uVar5 + 0x100000;
    } while (uVar5 < 0x900000);
    FUN_ffff00009077bfac(lVar2);
  }
  thunk_FUN_ffff0000907c314c(lVar1);
  return;
}


