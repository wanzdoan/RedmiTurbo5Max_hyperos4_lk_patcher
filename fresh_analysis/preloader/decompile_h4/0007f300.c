// File offset entry: 0x7f300

/* WARNING: Possible PIC construction at 0x0207f2e0: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0207f2fc: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0207f318: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0207f300) */
/* WARNING: Removing unreachable block (ram,0x0207f2e4) */
/* WARNING: Removing unreachable block (ram,0x0207f31c) */
/* WARNING: Removing unreachable block (ram,0x0207f3a4) */
/* WARNING: Removing unreachable block (ram,0x0207f420) */
/* WARNING: Removing unreachable block (ram,0x0207f3ac) */
/* WARNING: Removing unreachable block (ram,0x0207f43c) */
/* WARNING: Removing unreachable block (ram,0x0207f3d4) */
/* WARNING: Removing unreachable block (ram,0x0207f448) */
/* WARNING: Removing unreachable block (ram,0x0207f450) */
/* WARNING: Removing unreachable block (ram,0x0207f45c) */
/* WARNING: Removing unreachable block (ram,0x0207f3e0) */
/* WARNING: Removing unreachable block (ram,0x0207f460) */
/* WARNING: Removing unreachable block (ram,0x0207f488) */
/* WARNING: Removing unreachable block (ram,0x0207f468) */
/* WARNING: Removing unreachable block (ram,0x0207f3f4) */

int FUN_0207f200(long param_1)

{
  undefined1 in_ZR;
  int iVar1;
  long unaff_x19;
  undefined4 unaff_w21;
  undefined8 in_stack_00000010;
  
  func_0x0208ada0(param_1 + 0x927);
  func_0x02091c18(unaff_w21);
  func_0x0208ada0(0x20cd730,0x20c744a);
  if ((unaff_x19 == 0) || (iVar1 = func_0x020a4d44(), iVar1 == 0)) {
    func_0x0200a3bc(in_stack_00000010);
    func_0x0208c800();
    iVar1 = 0;
  }
  else {
    func_0x0208ada0(0x20b5bfb,iVar1);
  }
  func_0x0207f4b0();
  if ((bool)in_ZR) {
    return iVar1;
  }
  func_0x02091090();
  func_0x0208ada0(0x20b9fe8,0x20c744a);
  return 0x20cb7c2;
}


