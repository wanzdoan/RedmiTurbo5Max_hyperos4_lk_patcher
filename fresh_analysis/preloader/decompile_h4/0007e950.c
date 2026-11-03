// File offset entry: 0x7e950

/* WARNING: Possible PIC construction at 0x0207e970: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0207e974) */
/* WARNING: Removing unreachable block (ram,0x0207e99c) */
/* WARNING: Removing unreachable block (ram,0x0207e978) */
/* WARNING: Removing unreachable block (ram,0x0207e9a8) */
/* WARNING: Removing unreachable block (ram,0x0207e9fc) */
/* WARNING: Removing unreachable block (ram,0x0207e994) */
/* WARNING: Removing unreachable block (ram,0x0207e9d8) */
/* WARNING: Removing unreachable block (ram,0x0207e9e0) */
/* WARNING: Removing unreachable block (ram,0x0207ea18) */
/* WARNING: Removing unreachable block (ram,0x0207e9e8) */

void FUN_0207e850(int param_1,undefined8 param_2,long param_3,undefined8 param_4,ulong param_5)

{
  long *plVar1;
  undefined8 uVar2;
  ulong uVar3;
  uint uStack_4c;
  long lStack_48;
  
  param_5 = param_5 & 0xffffffff;
  lStack_48 = *plRam0000000002103280;
  uStack_4c = 0;
  plVar1 = (long *)FUN_0207e380(param_2);
  if (plVar1 == (long *)0x0) {
    uVar2 = 0x200000;
LAB_0207e8ec:
    func_0x02063328(uVar2);
  }
  else {
    uVar2 = func_0x0209132c(&uStack_4c);
    if ((int)uVar2 != 0) goto LAB_0207e8ec;
    uVar3 = plVar1[1] * (ulong)uStack_4c;
    if (param_3 + param_5 <= uVar3) {
      if (param_1 == 0) {
        uVar2 = func_0x02091394(3,param_3 + *plVar1 * (ulong)uStack_4c,param_4,param_5);
      }
      else {
        uVar2 = func_0x02091450();
      }
      goto code_r0x0207e8f8;
    }
    func_0x0208ada0(0x20b9fac,0x20c958b,param_2,uVar3,param_5);
  }
  uVar2 = 0xffffffffffffffff;
code_r0x0207e8f8:
  if (lRam0000000002100880 == lStack_48) {
    return;
  }
  func_0x02091090(uVar2);
  return;
}


