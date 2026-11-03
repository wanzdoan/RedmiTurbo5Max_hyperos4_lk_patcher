// File offset entry: 0x9142c

undefined4 FUN_0209132c(undefined4 *param_1)

{
  long lVar1;
  undefined4 uVar2;
  
  lVar1 = func_0x020036c8(1);
  if (lVar1 == 0) {
    uVar2 = 0x300000;
    FUN_02063328(0x300000);
    FUN_0208ada0(0x20b0cbf,0x20cec01,1);
  }
  else {
    uVar2 = 0;
    *param_1 = *(undefined4 *)(lVar1 + 4);
  }
  return uVar2;
}


