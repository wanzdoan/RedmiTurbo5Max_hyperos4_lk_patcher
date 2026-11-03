// ffff00009075f640  size=100  FUN_ffff00009075f640

void FUN_ffff00009075f640(long param_1)

{
  long lVar1;
  long lVar2;
  long lVar3;
  long lVar4;
  long lVar5;
  
  lVar1 = DAT_ffff0000908399e0;
  lVar2 = 0;
  lVar3 = param_1 + 8;
  lVar4 = DAT_ffff0000908399e0;
  do {
    lVar5 = 0;
    do {
      *(undefined1 *)(lVar4 + lVar5) = *(undefined1 *)(lVar3 + lVar5);
      lVar5 = lVar5 + 1;
    } while (lVar5 != 0x80);
    lVar5 = lVar2 * 4;
    lVar2 = lVar2 + 1;
    lVar3 = lVar3 + 0x80;
    lVar4 = lVar4 + 0x80;
    *(undefined4 *)(lVar1 + lVar5 + 0x2000) = *(undefined4 *)(param_1 + 0x2008 + lVar5);
  } while (lVar2 != 0x40);
  return;
}


