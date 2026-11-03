
undefined ** FUN_ffff0000be05df6c(undefined8 param_1)

{
  undefined **ppuVar1;
  char cVar2;
  bool bVar3;
  int iVar4;
  undefined **ppuVar5;
  
  FUN_ffff0000be01a730(&DAT_ffff0000be0ef2e8,0xffffffff);
  ppuVar5 = (undefined **)PTR_LOOP_ffff0000be0ef2e0;
  do {
    if (ppuVar5 == &PTR_LOOP_ffff0000be0ef2d8) {
      ppuVar5 = (undefined **)0x0;
LAB_ffff0000be05dfdc:
      FUN_ffff0000be01a7c8(&DAT_ffff0000be0ef2e8);
      return ppuVar5;
    }
    if ((ppuVar5[8] != (undefined *)0x0) &&
       (iVar4 = FUN_ffff0000be060cb8(ppuVar5[8],param_1), iVar4 == 0)) {
      ppuVar1 = ppuVar5 + 2;
      do {
        cVar2 = '\x01';
        bVar3 = (bool)ExclusiveMonitorPass(ppuVar1,0x10);
        if (bVar3) {
          *(int *)ppuVar1 = *(int *)ppuVar1 + 1;
          cVar2 = ExclusiveMonitorsStatus();
        }
      } while (cVar2 != '\0');
      goto LAB_ffff0000be05dfdc;
    }
    ppuVar5 = (undefined **)ppuVar5[1];
  } while( true );
}


