// File offset entry: 0xdb20

long * FUN_ffff0000b800db20(undefined8 param_1,long *param_2)

{
  long *plVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  long *plVar5;
  
  FUN_ffff0000b800ad90(&LAB_ffff0000b80acce8,0xffffffff);
  if (DAT_ffff0000b8115158 != 0) {
    plVar1 = (long *)(DAT_ffff0000b8114508 + 0x5780);
    plVar5 = (long *)(DAT_ffff0000b8114508 + 0xc18);
    do {
      if ((*plVar5 != 0) && (param_2 < plVar5)) {
        lVar3 = FUN_ffff0000b804cf40((long)plVar5 + 0x14);
        if ((lVar3 != 0) &&
           (((lVar4 = FUN_ffff0000b804cf40(param_1), lVar4 != 0 && (lVar3 == lVar4)) &&
            (iVar2 = FUN_ffff0000b804d038((long)plVar5 + 0x14,param_1,lVar3), iVar2 == 0))))
        goto code_r0xffff0000b800dbd8;
      }
      plVar5 = plVar5 + 0x13;
    } while (plVar5 <= plVar1);
  }
  plVar5 = (long *)0x0;
code_r0xffff0000b800dbd8:
  FUN_ffff0000b800ae28(&LAB_ffff0000b80acce8);
  return plVar5;
}


