// File offset entry: 0x8a8c4

void FUN_0208a7c4(void)

{
  undefined8 *****pppppuVar1;
  long lVar2;
  long *plVar3;
  undefined8 ****ppppuStack_38;
  undefined8 ****ppppuStack_30;
  long lStack_28;
  
  lStack_28 = *plRam0000000002103280;
  ppppuStack_38 = &ppppuStack_38;
  ppppuStack_30 = &ppppuStack_38;
  lVar2 = func_0x0208a6ac(1,&ppppuStack_38);
  pppppuVar1 = (undefined8 *****)0x0;
  if ((undefined8 *****)ppppuStack_30 != &ppppuStack_38 && lVar2 != 0) {
    pppppuVar1 = (undefined8 *****)ppppuStack_30;
  }
  if (*plRam0000000002103280 == lStack_28) {
    return;
  }
  plVar3 = (long *)FUN_02091090(pppppuVar1);
  *(long *)plVar3[1] = *plVar3;
  *(long *)(*plVar3 + 8) = plVar3[1];
  *plVar3 = 0;
  plVar3[1] = 0;
  return;
}


