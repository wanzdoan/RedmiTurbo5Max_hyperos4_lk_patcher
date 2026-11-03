
void FUN_ffff0000be021074(code *param_1)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  lVar1 = FUN_ffff0000be05f1bc(0x100000);
  if (lVar1 == 0) {
    FUN_ffff0000be05fd98
              (s_dramc__malloc_failed_at__s_ffff0000be0ac70d,s_save_fullk_data_ffff0000be0b9b26);
    return;
  }
  lVar2 = FUN_ffff0000be05df6c(s_dram_para_ffff0000be0ad5f7);
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98
              (s_dramc__Partition___s__is_not_exi_ffff0000be0ad601,s_dram_para_ffff0000be0ad5f7);
  }
  else {
    uVar5 = 0xfffffffffff00000;
    do {
      FUN_ffff0000be060ae0(lVar1,0,0x100000);
      uVar3 = FUN_ffff0000be05e000(lVar2,lVar1,uVar5 + 0x200000,0x100000);
      if (uVar3 >> 0x14 == 0) {
        FUN_ffff0000be063b40(s_dramc__WARN___read_size_from_dra_ffff0000be0a333a,uVar3);
        FUN_ffff0000be05fd98(s_dramc__WARN___read_size_from_dra_ffff0000be0a333a,uVar3);
        break;
      }
      uVar4 = FUN_ffff0000be05d228(lVar1);
      uVar3 = (*param_1)(uVar4,uVar3);
      if ((uVar3 & 1) == 0) {
        FUN_ffff0000be05fd98
                  (s__s__failed_to_set_fullk_data__ffff0000be0b3fda,
                   s_save_fullk_data_ffff0000be0b9b26);
      }
      uVar5 = uVar5 + 0x100000;
    } while (uVar5 < 0x900000);
    FUN_ffff0000be05def0(lVar2);
  }
  thunk_FUN_ffff0000be09937c(lVar1);
  return;
}


