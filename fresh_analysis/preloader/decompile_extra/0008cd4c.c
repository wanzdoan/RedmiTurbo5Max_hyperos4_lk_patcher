// File offset entry: 0x8cd4c

undefined8 FUN_0208cc4c(void)

{
  char cVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  undefined8 uVar5;
  
  uVar3 = FUN_020667f8(0);
  if ((uVar3 & 1) == 0) {
    return 0;
  }
  if (cRam0000000002120a70 == '\0') {
    cVar1 = func_0x0208c25c(0);
    if (cVar1 != '\0') {
      FUN_0208ada0(0x20ceabf,0x20ccb16);
      thunk_FUN_02091ce4(0x5dc);
      uVar3 = FUN_020667f8(0);
      if ((uVar3 & 1) == 0) {
        FUN_0208ada0(0x20cd8eb,0x20ccb16);
        thunk_FUN_02091ce4(0x5dc);
        uVar3 = FUN_020667f8(0);
        if ((uVar3 & 1) != 0) {
          FUN_0208ada0(0x20b9009,0x20ccb16);
          FUN_02091664(0x211da70,0,0x800);
          lVar4 = FUN_0207e954(0x20cb9dd,0,0x211da70,0x800);
          if ((lVar4 != 0x800) && (iVar2 = func_0x020632ac(), iVar2 != 0)) {
            FUN_0208ada0(0x20b6de6,0x20ccb16,0);
            return 0;
          }
          FUN_0208ada0(0x20b901f,0x20ccb16);
          return 1;
        }
        uVar5 = 0x20bd017;
      }
      else {
        uVar5 = 0x20b9009;
      }
      FUN_0208ada0(uVar5,0x20ccb16);
    }
    return 0;
  }
  return 0;
}


