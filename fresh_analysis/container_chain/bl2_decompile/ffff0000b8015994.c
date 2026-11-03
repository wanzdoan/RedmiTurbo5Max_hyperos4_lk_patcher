
void FUN_ffff0000b8015994(code *param_1)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  lVar1 = FUN_ffff0000b804bccc(0x100000);
  if (lVar1 == 0) {
    FUN_ffff0000b804c33c
              (s_dramc__malloc_failed_at__s_ffff0000b808c503,s_save_fullk_data_ffff0000b8096fdc);
    return;
  }
  lVar2 = FUN_ffff0000b804a99c(s_dram_para_ffff0000b808cef4);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c
              (s_dramc__Partition___s__is_not_exi_ffff0000b808cefe,s_dram_para_ffff0000b808cef4);
  }
  else {
    uVar5 = 0xfffffffffff00000;
    do {
      FUN_ffff0000b804d070(lVar1,0,0x100000);
      uVar3 = FUN_ffff0000b804aa30(lVar2,lVar1,uVar5 + 0x200000,0x100000);
      if (uVar3 >> 0x14 == 0) {
        FUN_ffff0000b804f4a8(s_dramc__WARN___read_size_from_dra_ffff0000b808517e,uVar3);
        FUN_ffff0000b804c33c(s_dramc__WARN___read_size_from_dra_ffff0000b808517e,uVar3);
        break;
      }
      uVar4 = FUN_ffff0000b8049ec8(lVar1);
      uVar3 = (*param_1)(uVar4,uVar3);
      if ((uVar3 & 1) == 0) {
        FUN_ffff0000b804c33c
                  (s__s__failed_to_set_fullk_data__ffff0000b8092493,
                   s_save_fullk_data_ffff0000b8096fdc);
      }
      uVar5 = uVar5 + 0x100000;
    } while (uVar5 < 0x900000);
    FUN_ffff0000b804a920(lVar2);
  }
  thunk_FUN_ffff0000b8082558(lVar1);
  return;
}


