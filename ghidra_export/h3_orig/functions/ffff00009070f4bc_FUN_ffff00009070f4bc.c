// ffff00009070f4bc  size=220  FUN_ffff00009070f4bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070f4bc(void)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  
  FUN_ffff000090717454
            (_DAT_ffff00009083e068 + (ulong)*(uint *)(_DAT_ffff00009083e068 + 0xf8) * 0x78 + 0x20,
             0xffffffff);
  iVar1 = *(int *)(_DAT_ffff00009083e068 + 0x160);
  do {
    lVar2 = _DAT_ffff00009083e068;
    if (iVar1 < 0) {
LAB_ffff00009070f588:
      FUN_ffff00009070f270(lVar2,1);
                    /* WARNING: Subroutine does not return */
      FUN_ffff000090717ec0(0);
    }
    lVar2 = _DAT_ffff00009083e068 + (ulong)*(uint *)(_DAT_ffff00009083e068 + 0xf8) * 0x78;
    iVar1 = FUN_ffff00009070f2ec
                      (*(long *)(lVar2 + 0x10) - (ulong)*(uint *)(lVar2 + 0x18),
                       *(uint *)(lVar2 + 0x18) + *(int *)(lVar2 + 0x1c));
    lVar2 = _DAT_ffff00009083e068;
    *(int *)(_DAT_ffff00009083e068 + 0x164) = iVar1;
    if (iVar1 != 0) {
      FUN_ffff00009077de54(s_write_data_failed__handle_status_ffff0000907fe8fe);
      lVar2 = _DAT_ffff00009083e068;
      goto LAB_ffff00009070f588;
    }
    if (*(int *)(lVar2 + (ulong)*(uint *)(lVar2 + 0xf8) * 0x78 + 0x1c) == 0)
    goto LAB_ffff00009070f588;
    FUN_ffff0000907174e0(lVar2 + (ulong)*(uint *)(lVar2 + 0xf8) * 0x78 + 0x50,0);
    uVar3 = *(uint *)(_DAT_ffff00009083e068 + 0xf8) ^ 1;
    lVar2 = _DAT_ffff00009083e068 + (ulong)uVar3 * 0x78;
    *(uint *)(_DAT_ffff00009083e068 + 0xf8) = uVar3;
    FUN_ffff000090717454(lVar2 + 0x20,0xffffffff);
    iVar1 = *(int *)(_DAT_ffff00009083e068 + 0x160);
  } while( true );
}


