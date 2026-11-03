// ffff000090751660  size=4  FUN_ffff000090751660

undefined8 FUN_ffff000090751660(int param_1)

{
  long lVar1;
  ulong uVar2;
  long *plVar3;
  
  uVar2 = (ulong)(param_1 != 0x2d && param_1 != 0x2b);
  if ((*(byte *)((&DAT_ffff00009080cd68)[uVar2] + 0x34) & 3) != 0) {
    lVar1 = 1;
    if (param_1 != 0x2b) {
      lVar1 = 2;
    }
    lVar1 = lVar1 - uVar2;
    plVar3 = (long *)(s_0123456789abcdef_ffff000090804a48 + uVar2 * 8 + 0x10);
    do {
      lVar1 = lVar1 + -1;
      *(uint *)(*plVar3 + 0x34) = *(uint *)(*plVar3 + 0x34) & 0xfffffffc;
      plVar3 = plVar3 + 1;
    } while (lVar1 != 0);
    FUN_ffff00009077de54
              (s___s__set_DISP_REG_DSI_MODE_CON___ffff0000907db0ef,s_DSI_SetMode_ffff0000907e9713,0)
    ;
  }
  return 0;
}


