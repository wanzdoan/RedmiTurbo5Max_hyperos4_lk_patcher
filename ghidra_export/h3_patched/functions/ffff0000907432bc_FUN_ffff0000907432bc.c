// ffff0000907432bc  size=196  FUN_ffff0000907432bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907432bc(long param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  uint *puVar5;
  int local_24;
  
  local_24 = 0;
  if ((param_1 != 0) &&
     (iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mmup_ffff0000907fba07), -1 < iVar2)
     ) {
    lVar4 = FUN_ffff00009076dcd4(param_1,iVar2,s_status_ffff0000907edb53,&local_24);
    if (lVar4 == 0) {
      DAT_ffff00009087745c = false;
    }
    else {
      iVar3 = FUN_ffff00009077f080(lVar4,&DAT_ffff0000907df58f,(long)local_24);
      DAT_ffff00009087745c = iVar3 == 0;
    }
    puVar5 = (uint *)FUN_ffff00009076dcd4(param_1,iVar2,s_l2tcm_offset_ffff0000907d483d,&local_24);
    if (puVar5 == (uint *)0x0) {
      _DAT_ffff000090877460 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      _DAT_ffff000090877460 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
  }
  return;
}


