
void FUN_ffff00009077d31c(long *param_1)

{
  long *plVar1;
  undefined8 uVar2;
  ulong uVar3;
  long *plVar4;
  uint uVar5;
  
  do {
    FUN_ffff000090717598(param_1 + 2,0xffffffff);
    uVar2 = daif;
    uVar5 = (uint)uVar2;
    while( true ) {
      if ((uVar5 >> 7 & 1) == 0) {
        uVar3 = daif;
        daif = uVar3 | 0x80;
      }
      plVar4 = (long *)param_1[1];
      if (plVar4 == param_1) {
        plVar4 = (long *)0x0;
        param_1[8] = 0;
      }
      else {
        plVar1 = (long *)plVar4[1];
        *plVar1 = *plVar4;
        *(long **)(*plVar4 + 8) = plVar1;
        *plVar4 = 0;
        plVar4[1] = 0;
        param_1[8] = 0;
      }
      if (((uint)uVar2 >> 7 & 1) == 0) {
        uVar3 = daif;
        daif = uVar3 & 0xffffffffffffff7f;
      }
      if (plVar4 == (long *)0x0) break;
      (*(code *)plVar4[2])();
      uVar2 = daif;
      uVar5 = (uint)uVar2;
    }
  } while( true );
}


