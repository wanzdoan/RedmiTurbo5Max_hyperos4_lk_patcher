// File offset entry: 0x5cc14

/* WARNING: Type propagation algorithm not settling */

undefined8 FUN_0205cb14(long param_1)

{
  undefined4 uVar1;
  long lVar2;
  long lVar3;
  undefined8 uVar4;
  undefined1 auVar5 [16];
  
  uVar1 = func_0x02091294(3);
  if (param_1 == 0) {
    uVar4 = 0x20cc503;
  }
  else {
    lVar2 = func_0x020036c8(1);
    if (lVar2 != 0) {
      uRam0000000000103120 = 0x103128;
      lVar3 = func_0x0205cc04(uVar1,1,param_1,*(undefined4 *)(lVar2 + 4));
      if (lVar3 != 0) {
        uVar4 = func_0x0205cef8(uVar1);
        lVar2 = func_0x0205cc04(uVar1,uVar4,param_1,*(undefined4 *)(lVar2 + 4));
        if (lVar2 != 0) {
          FUN_0208ada0(0x20bfc67,0x20ca029);
          return 0;
        }
      }
      if (lRam0000000000103100 != 0 || lRam0000000000103108 != 0) {
        return 0x103100;
      }
      return 0;
    }
    uVar4 = 0x20b587e;
  }
  auVar5 = func_0x0205d900(uVar4);
  FUN_0208ada0(auVar5._0_8_,auVar5._8_8_,0x20c1d22);
  return 0;
}


