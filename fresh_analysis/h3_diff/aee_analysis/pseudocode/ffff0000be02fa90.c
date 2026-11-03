
void FUN_ffff0000be02fa90(long param_1)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined4 local_4;
  
  local_4 = 0;
  if (param_1 == 0) {
    FUN_ffff0000be05fd98(s_slbc__failed_to_get_fdt_ffff0000be0c1d62);
    DAT_ffff0000be119568 = 0xfffffffd;
  }
  else {
    iVar2 = FUN_ffff0000be056824(param_1,0xffffffff,s_mediatek_mtk_slbc_ffff0000be0c1d7b);
    if (iVar2 < 0) {
      FUN_ffff0000be05fd98(s_slbc__failed_to_get_slbc_fdt_off_ffff0000be0be478);
      DAT_ffff0000be119568 = 0xfffffffc;
    }
    else {
      lVar3 = FUN_ffff0000be056494(param_1,iVar2,&DAT_ffff0000be0ae6fc,&local_4);
      if (lVar3 == 0) {
        FUN_ffff0000be05fd98(s_slbc__failed_to_get_slbc_sram_ffff0000be0c1d8d);
        DAT_ffff0000be11956c = 1;
      }
      else {
        uVar1 = (*(uint *)(lVar3 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 4) & 0xff00ff) << 8;
        DAT_ffff0000be119570 = uVar1 >> 0x10 | uVar1 << 0x10;
        uVar1 = (*(uint *)(lVar3 + 0xc) & 0xff00ff00) >> 8 |
                (*(uint *)(lVar3 + 0xc) & 0xff00ff) << 8;
        DAT_ffff0000be119574 = uVar1 >> 0x10 | uVar1 << 0x10;
      }
    }
  }
  return;
}


