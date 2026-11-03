
long * FUN_ffff0000be01cb9c(undefined8 param_1,long *param_2)

{
  long *plVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  long *plVar5;
  
  FUN_ffff0000be01a730(&LAB_ffff0000be0d6218,0xffffffff);
  if (DAT_ffff0000be112910 != 0) {
    plVar1 = (long *)(DAT_ffff0000be111cc8 + 0x5780);
    plVar5 = (long *)(DAT_ffff0000be111cc8 + 0xc18);
    do {
      if ((*plVar5 != 0) && (param_2 < plVar5)) {
        lVar3 = FUN_ffff0000be060e5c((long)plVar5 + 0x14);
        if ((lVar3 != 0) &&
           (((lVar4 = FUN_ffff0000be060e5c(param_1), lVar4 != 0 && (lVar3 == lVar4)) &&
            (iVar2 = FUN_ffff0000be060f54((long)plVar5 + 0x14,param_1,lVar3), iVar2 == 0))))
        goto code_r0xffff0000be01cc54;
      }
      plVar5 = plVar5 + 0x13;
    } while (plVar5 <= plVar1);
  }
  plVar5 = (long *)0x0;
code_r0xffff0000be01cc54:
  FUN_ffff0000be01a7c8(&LAB_ffff0000be0d6218);
  return plVar5;
}


