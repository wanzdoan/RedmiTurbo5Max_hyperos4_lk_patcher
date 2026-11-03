// ffff00009071a348  size=220  FUN_ffff00009071a348

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long * FUN_ffff00009071a348(undefined8 param_1,long *param_2)

{
  long *plVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  long *plVar5;
  
  FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
  if (_DAT_ffff00009085b8e0 != 0) {
    plVar1 = (long *)(_DAT_ffff000090850c90 + 0x5780);
    plVar5 = (long *)(_DAT_ffff000090850c90 + 0xc18);
    do {
      if ((*plVar5 != 0) && (param_2 < plVar5)) {
        lVar3 = FUN_ffff00009077f250((long)plVar5 + 0x14);
        if ((lVar3 != 0) &&
           (((lVar4 = FUN_ffff00009077f250(param_1), lVar4 != 0 && (lVar3 == lVar4)) &&
            (iVar2 = FUN_ffff00009077f348((long)plVar5 + 0x14,param_1,lVar3), iVar2 == 0))))
        goto code_r0xffff00009071a400;
      }
      plVar5 = plVar5 + 0x13;
    } while (plVar5 <= plVar1);
  }
  plVar5 = (long *)0x0;
code_r0xffff00009071a400:
  FUN_ffff0000907177d0(&LAB_ffff000090818208);
  return plVar5;
}


