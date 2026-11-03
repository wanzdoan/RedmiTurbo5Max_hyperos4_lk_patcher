// File offset entry: 0xa4e44

undefined8 FUN_020a4d44(undefined8 param_1,undefined8 param_2,int *param_3)

{
  bool bVar1;
  undefined4 uVar2;
  int iVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  long lVar6;
  
  uVar2 = func_0x0208cbd0();
  *param_3 = 0;
  uVar4 = func_0x0208cbb0();
  func_0x02091664(uVar4,0,uVar2);
  func_0x02091664(0x2123170,0,0x8c);
  bVar1 = false;
  lVar6 = 0;
  while (!bVar1) {
    uVar5 = func_0x0208cbfc(param_1,uVar4,lVar6,0x800);
    if ((int)uVar5 != 0) {
      return uVar5;
    }
    uVar5 = func_0x02066430(uVar4,0x800,0x2123170);
    if ((int)uVar5 != 0) {
      return uVar5;
    }
    bVar1 = false;
    if (iRam0000000002123170 == 1) {
      iVar3 = func_0x020a4ee4(param_2);
      bVar1 = iVar3 == 0;
      lVar6 = lVar6 + (ulong)uRam0000000002123174 + (ulong)uRam0000000002123194;
    }
  }
  *param_3 = *param_3 + uRam0000000002123194;
  uVar5 = func_0x0208cbfc(param_1,uVar4,lVar6,0x800);
  iVar3 = (int)uVar5;
  while( true ) {
    if (iVar3 != 0) {
      return uVar5;
    }
    iVar3 = func_0x02066430(uVar4,0x800,0x2123170);
    if (iVar3 != 0) {
      return 0;
    }
    if (iRam0000000002123170 != 1) {
      return 0;
    }
    uVar5 = func_0x020a72a8(uRam000000000212317c);
    if ((int)uVar5 == 0) break;
    iVar3 = *param_3 + uRam0000000002123174;
    *param_3 = iVar3;
    *param_3 = uRam0000000002123194 + iVar3;
    lVar6 = lVar6 + (ulong)uRam0000000002123174 + (ulong)uRam0000000002123194;
    uVar5 = func_0x0208cbfc(param_1,uVar4,lVar6,0x800);
    iVar3 = (int)uVar5;
  }
  return uVar5;
}


