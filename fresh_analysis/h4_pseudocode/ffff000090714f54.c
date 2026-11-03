
undefined8 FUN_ffff000090714f54(int param_1,uint param_2)

{
  long lVar1;
  long lVar2;
  long lVar3;
  char *pcVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  lVar1 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
  if (lVar1 == 0) {
    pcVar4 = s__blackbox__EBlackboxBootfail__pa_ffff0000908020b9;
LAB_ffff000090714ff0:
    FUN_ffff00009077e11c(pcVar4);
    uVar5 = 1;
  }
  else {
    lVar2 = FUN_ffff00009077d514(0x1000);
    if (lVar2 == 0) {
      FUN_ffff00009077e11c(s__blackbox__EBlackboxBootfail__Al_ffff0000907f7cee);
    }
    else {
      FUN_ffff00009077ee50(lVar2,0,0x1000);
      if (0xfff < param_2) {
        param_2 = param_2 >> 0xc;
        if (param_2 < 2) {
          param_2 = 1;
        }
        uVar6 = (ulong)param_2;
        do {
          lVar3 = FUN_ffff00009077c144(lVar1,lVar2,param_1,0x1000);
          if (lVar3 < 1) {
            pcVar4 = s__blackbox__EBlackboxBootfail__pa_ffff0000907f950f;
            goto LAB_ffff000090714ff0;
          }
          param_1 = param_1 + 0x1000;
          uVar6 = uVar6 - 1;
        } while (uVar6 != 0);
      }
      thunk_FUN_ffff0000907c314c(lVar2);
    }
    uVar5 = 0;
  }
  return uVar5;
}


