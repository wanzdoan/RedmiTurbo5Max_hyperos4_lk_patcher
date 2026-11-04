
undefined ** FUN_ffff00009077c990(undefined8 param_1)

{
  undefined **ppuVar1;
  char cVar2;
  bool bVar3;
  int iVar4;
  undefined **ppuVar5;

  FUN_ffff000090716f9c(&DAT_ffff00009082a8a8,0xffffffff);
  ppuVar5 = (undefined **)PTR_LOOP_ffff00009082a8a0;
  do {
    if (ppuVar5 == &PTR_LOOP_ffff00009082a898) {
      ppuVar5 = (undefined **)0x0;
LAB_ffff00009077ca00:
      FUN_ffff000090717060(&DAT_ffff00009082a8a8);
      return ppuVar5;
    }
    if ((ppuVar5[8] != (undefined *)0x0) &&
       (iVar4 = FUN_ffff00009077f990(ppuVar5[8],param_1), iVar4 == 0)) {
      ppuVar1 = ppuVar5 + 2;
      do {
        cVar2 = '\x01';
        bVar3 = (bool)ExclusiveMonitorPass(ppuVar1,0x10);
        if (bVar3) {
          *(int *)ppuVar1 = *(int *)ppuVar1 + 1;
          cVar2 = ExclusiveMonitorsStatus();
        }
      } while (cVar2 != '\0');
      goto LAB_ffff00009077ca00;
    }
    ppuVar5 = (undefined **)ppuVar5[1];
  } while( true );
}
