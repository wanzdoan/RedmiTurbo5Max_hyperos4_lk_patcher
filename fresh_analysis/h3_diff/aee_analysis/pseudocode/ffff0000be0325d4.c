
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be0325d4(code *param_1)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  ulong uVar5;
  long lVar6;
  
  lVar3 = FUN_ffff0000be05f1bc(0x1000);
  if (lVar3 != 0) {
    iVar2 = FUN_ffff0000be0611dc
                      (lVar3,0x1000,s_DVFSRC_regs_0x_x____0x_08x_ffff0000be0a49f4,0x1c0133b0,
                       _DAT_ffff00001c0133b0);
    lVar6 = lVar3 + iVar2;
    uVar5 = 0;
    do {
      iVar2 = FUN_ffff0000be0611dc
                        (lVar6,(lVar3 + 0x1000) - lVar6,
                         s_DVFSRC_regs_0x_x____0x_08x_ffff0000be0a49f4,(int)uVar5 + 0x1c0134b8,
                         *(undefined4 *)(uVar5 - 0xffffe3fecb48));
      bVar1 = uVar5 < 0xfc;
      lVar6 = lVar6 + iVar2;
      uVar5 = uVar5 + 4;
    } while (bVar1);
    uVar4 = FUN_ffff0000be05d228(lVar3);
    uVar5 = (*param_1)(uVar4,lVar6 - lVar3);
    if ((uVar5 & 1) == 0) {
      FUN_ffff0000be05fd98(s__dvfsrc__AEE_EXPDB_dump_fail_ffff0000be0a80dd);
    }
    thunk_FUN_ffff0000be09937c(lVar3);
    return;
  }
  FUN_ffff0000be05fd98(s__dvfsrc__can_not_alloc_buffer_ffff0000be0aa4fa);
  return;
}


