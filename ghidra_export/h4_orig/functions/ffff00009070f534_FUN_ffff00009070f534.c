// ffff00009070f534  size=220  FUN_ffff00009070f534

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070f534(void)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  
  FUN_ffff000090717598
            (_DAT_ffff00009083e0a0 + (ulong)*(uint *)(_DAT_ffff00009083e0a0 + 0xf8) * 0x78 + 0x20,
             0xffffffff);
  iVar1 = *(int *)(_DAT_ffff00009083e0a0 + 0x160);
  do {
    lVar2 = _DAT_ffff00009083e0a0;
    if (iVar1 < 0) {
LAB_ffff00009070f600:
      FUN_ffff00009070f2e8(lVar2,1);
                    /* WARNING: Subroutine does not return */
      FUN_ffff000090718004(0);
    }
    lVar2 = _DAT_ffff00009083e0a0 + (ulong)*(uint *)(_DAT_ffff00009083e0a0 + 0xf8) * 0x78;
    iVar1 = FUN_ffff00009070f364
                      (*(long *)(lVar2 + 0x10) - (ulong)*(uint *)(lVar2 + 0x18),
                       *(uint *)(lVar2 + 0x18) + *(int *)(lVar2 + 0x1c));
    lVar2 = _DAT_ffff00009083e0a0;
    *(int *)(_DAT_ffff00009083e0a0 + 0x164) = iVar1;
    if (iVar1 != 0) {
      FUN_ffff00009077e11c(s_write_data_failed__handle_status_ffff0000907feaa6);
      lVar2 = _DAT_ffff00009083e0a0;
      goto LAB_ffff00009070f600;
    }
    if (*(int *)(lVar2 + (ulong)*(uint *)(lVar2 + 0xf8) * 0x78 + 0x1c) == 0)
    goto LAB_ffff00009070f600;
    FUN_ffff000090717624(lVar2 + (ulong)*(uint *)(lVar2 + 0xf8) * 0x78 + 0x50,0);
    uVar3 = *(uint *)(_DAT_ffff00009083e0a0 + 0xf8) ^ 1;
    lVar2 = _DAT_ffff00009083e0a0 + (ulong)uVar3 * 0x78;
    *(uint *)(_DAT_ffff00009083e0a0 + 0xf8) = uVar3;
    FUN_ffff000090717598(lVar2 + 0x20,0xffffffff);
    iVar1 = *(int *)(_DAT_ffff00009083e0a0 + 0x160);
  } while( true );
}


