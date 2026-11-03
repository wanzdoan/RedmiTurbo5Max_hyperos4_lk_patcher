
void FUN_ffff0000be018784(void)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  
  FUN_ffff0000be01a590
            (DAT_ffff0000be108810 + (ulong)*(uint *)(DAT_ffff0000be108810 + 0xf8) * 0x78 + 0x20,
             0xffffffff);
  iVar1 = *(int *)(DAT_ffff0000be108810 + 0x160);
  do {
    lVar2 = DAT_ffff0000be108810;
    if (iVar1 < 0) {
LAB_ffff0000be018850:
      FUN_ffff0000be018538(lVar2,1);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be01afc4(0);
    }
    lVar2 = DAT_ffff0000be108810 + (ulong)*(uint *)(DAT_ffff0000be108810 + 0xf8) * 0x78;
    iVar1 = FUN_ffff0000be0185b4
                      (*(long *)(lVar2 + 0x10) - (ulong)*(uint *)(lVar2 + 0x18),
                       *(uint *)(lVar2 + 0x18) + *(int *)(lVar2 + 0x1c));
    lVar2 = DAT_ffff0000be108810;
    *(int *)(DAT_ffff0000be108810 + 0x164) = iVar1;
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98(s_write_data_failed__handle_status_ffff0000be0c3dad);
      lVar2 = DAT_ffff0000be108810;
      goto LAB_ffff0000be018850;
    }
    if (*(int *)(lVar2 + (ulong)*(uint *)(lVar2 + 0xf8) * 0x78 + 0x1c) == 0)
    goto LAB_ffff0000be018850;
    FUN_ffff0000be01a61c(lVar2 + (ulong)*(uint *)(lVar2 + 0xf8) * 0x78 + 0x50,0);
    uVar3 = *(uint *)(DAT_ffff0000be108810 + 0xf8) ^ 1;
    lVar2 = DAT_ffff0000be108810 + (ulong)uVar3 * 0x78;
    *(uint *)(DAT_ffff0000be108810 + 0xf8) = uVar3;
    FUN_ffff0000be01a590(lVar2 + 0x20,0xffffffff);
    iVar1 = *(int *)(DAT_ffff0000be108810 + 0x160);
  } while( true );
}


