// ffff0000907b90f8  size=264  FUN_ffff0000907b90f8

undefined8 FUN_ffff0000907b90f8(long param_1,undefined1 *param_2,ulong param_3)

{
  bool bVar1;
  undefined8 uVar2;
  ulong uVar3;
  undefined1 *puVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  
  uVar2 = 0x7247;
  if ((param_1 != 0) && (param_2 != (undefined1 *)0x0)) {
    uVar3 = *(ulong *)(param_1 + 0xb8);
    uVar7 = uVar3 & 0x7f;
    uVar6 = 0x80 - uVar7;
    *(ulong *)(param_1 + 0xb8) = uVar3 + param_3;
    while( true ) {
      for (; (uVar7 == 0 ||
             (puVar4 = param_2, uVar5 = uVar3 & 0x7f | 0xffffffffffffff80, param_3 < uVar6));
          param_3 = param_3 & 0x7f) {
        if (param_3 < 0x80) {
          if (param_3 != 0) {
            puVar4 = (undefined1 *)(param_1 + uVar7);
            do {
              param_3 = param_3 - 1;
              *puVar4 = *param_2;
              puVar4 = puVar4 + 1;
              param_2 = param_2 + 1;
            } while (param_3 != 0);
          }
          return 0;
        }
        uVar2 = FUN_ffff0000907b9200(param_1,param_2,param_3 & 0xffffffffffffff80);
        if ((int)uVar2 != 0) {
          return uVar2;
        }
        param_2 = param_2 + (param_3 & 0xffffffffffffff80);
      }
      do {
        bVar1 = uVar5 != 0xffffffffffffffff;
        *(undefined1 *)(param_1 + uVar7) = *puVar4;
        uVar7 = uVar7 + 1;
        puVar4 = puVar4 + 1;
        uVar5 = uVar5 + 1;
      } while (bVar1);
      uVar2 = FUN_ffff0000907b9200(param_1,param_1,0x80);
      if ((int)uVar2 != 0) break;
      uVar7 = 0;
      param_2 = param_2 + uVar6;
      param_3 = param_3 - uVar6;
    }
  }
  return uVar2;
}


