// ffff000090723994  size=328  FUN_ffff000090723994

void FUN_ffff000090723994(code *param_1)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  lVar1 = FUN_ffff00009077d24c(0x100000);
  if (lVar1 == 0) {
    FUN_ffff00009077de54
              (s_dramc__malloc_failed_at__s_ffff0000907dc44e,s_save_fullk_data_ffff0000907ef4f3);
    return;
  }
  lVar2 = FUN_ffff00009077bd60(s_dram_para_ffff0000907ddbb8);
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s_dramc__Partition___s__is_not_exi_ffff0000907ddbc2,s_dram_para_ffff0000907ddbb8);
  }
  else {
    uVar5 = 0xfffffffffff00000;
    do {
      FUN_ffff00009077eb88(lVar1,0,0x100000);
      uVar3 = FUN_ffff00009077bdf4(lVar2,lVar1,uVar5 + 0x200000,0x100000);
      if (uVar3 >> 0x14 == 0) {
        FUN_ffff000090781820(s_dramc__WARN___read_size_from_dra_ffff0000907cf414,uVar3);
        FUN_ffff00009077de54(s_dramc__WARN___read_size_from_dra_ffff0000907cf414,uVar3);
        break;
      }
      uVar4 = FUN_ffff00009077b06c(lVar1);
      uVar3 = (*param_1)(uVar4,uVar3);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54
                  (s__s__failed_to_set_fullk_data__ffff0000907e75dc,
                   s_save_fullk_data_ffff0000907ef4f3);
      }
      uVar5 = uVar5 + 0x100000;
    } while (uVar5 < 0x900000);
    FUN_ffff00009077bce4(lVar2);
  }
  thunk_FUN_ffff0000907c2ad8(lVar1);
  return;
}


