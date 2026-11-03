// File offset entry: 0x8c6b0

uint FUN_0208c5b0(void)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  FUN_0208ada0(0x20c4393);
  uVar1 = func_0x0207d8c8(0x20bd9b4,0x211da58);
  if (uVar1 != 0) {
    FUN_0208ada0(0x20cd8da);
  }
  uVar2 = func_0x0207d9e4(0x211da58,1800000,1800000);
  if ((uVar2 | uVar1) != 0) {
    FUN_0208ada0(0x20c9777);
  }
  uVar3 = func_0x0207d99c(0x211da58,1);
  uVar3 = uVar3 | uVar2 | uVar1;
  if (uVar3 != 0) {
    FUN_0208ada0(0x20c9777);
  }
  func_0x02091d50(10);
  return uVar3;
}


