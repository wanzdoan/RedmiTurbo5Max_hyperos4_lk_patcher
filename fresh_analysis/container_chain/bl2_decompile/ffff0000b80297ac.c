
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b80297ac(code *param_1)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  ulong uVar5;
  long lVar6;
  
  lVar3 = FUN_ffff0000b804bccc(0x1000);
  if (lVar3 != 0) {
    iVar2 = FUN_ffff0000b804d74c
                      (lVar3,0x1000,s_DVFSRC_regs_0x_x____0x_08x_ffff0000b808654a,0x1c0133b0,
                       _DAT_ffff00001c0133b0);
    lVar6 = lVar3 + iVar2;
    uVar5 = 0;
    do {
      iVar2 = FUN_ffff0000b804d74c
                        (lVar6,(lVar3 + 0x1000) - lVar6,
                         s_DVFSRC_regs_0x_x____0x_08x_ffff0000b808654a,(int)uVar5 + 0x1c0134b8,
                         *(undefined4 *)(uVar5 - 0xffffe3fecb48));
      bVar1 = uVar5 < 0xfc;
      lVar6 = lVar6 + iVar2;
      uVar5 = uVar5 + 4;
    } while (bVar1);
    uVar4 = FUN_ffff0000b8049ec8(lVar3);
    uVar5 = (*param_1)(uVar4,lVar6 - lVar3);
    if ((uVar5 & 1) == 0) {
      FUN_ffff0000b804c33c(s__dvfsrc__AEE_EXPDB_dump_fail_ffff0000b8088ff1);
    }
    thunk_FUN_ffff0000b8082558(lVar3);
    return;
  }
  FUN_ffff0000b804c33c(s__dvfsrc__can_not_alloc_buffer_ffff0000b808ab00);
  return;
}


