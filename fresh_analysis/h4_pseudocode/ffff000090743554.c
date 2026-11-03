
void FUN_ffff000090743554(long param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  uint *puVar5;
  int local_24;
  
  local_24 = 0;
  if ((param_1 != 0) &&
     (iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_mmup_ffff0000907fbba3), -1 < iVar2)
     ) {
    lVar4 = FUN_ffff00009076df9c(param_1,iVar2,s_status_ffff0000907edcdb,&local_24);
    if (lVar4 == 0) {
      DAT_ffff0000908774f4 = false;
    }
    else {
      iVar3 = FUN_ffff00009077f348(lVar4,&DAT_ffff0000907df649,(long)local_24);
      DAT_ffff0000908774f4 = iVar3 == 0;
    }
    puVar5 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_l2tcm_offset_ffff0000907d487a,&local_24);
    if (puVar5 == (uint *)0x0) {
      DAT_ffff0000908774f8 = 0;
    }
    else {
      uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
      DAT_ffff0000908774f8 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
  }
  return;
}


