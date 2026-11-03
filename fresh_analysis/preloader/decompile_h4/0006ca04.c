// File offset entry: 0x6ca04

long FUN_0206c904(undefined8 param_1,ulong param_2)

{
  long *plVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  undefined8 uVar5;
  ulong uVar6;
  long lVar7;
  
  plVar1 = plRam0000000002103338;
  uVar6 = 0;
  lVar7 = 0x7d700;
  do {
    if (uVar6 == 0x80) {
      return 0;
    }
    if ((((param_2 == 0) || (param_2 < uVar6)) && (*(long *)(*plVar1 + lVar7) != 0)) &&
       ((lVar3 = FUN_02091524(*plVar1 + lVar7 + 0x14), lVar3 != 0 &&
        (lVar3 = FUN_02091524(param_1), lVar3 != 0)))) {
      lVar3 = FUN_02091524(*plVar1 + lVar7 + 0x14);
      lVar4 = FUN_02091524(param_1);
      if (lVar3 == lVar4) {
        lVar3 = *plVar1;
        uVar5 = FUN_02091524(param_1);
        iVar2 = FUN_0209156c(lVar3 + lVar7 + 0x14,param_1,uVar5);
        if (iVar2 == 0) {
          return *plVar1 + lVar7;
        }
      }
    }
    uVar6 = uVar6 + 1;
    lVar7 = lVar7 + 0x98;
  } while( true );
}


